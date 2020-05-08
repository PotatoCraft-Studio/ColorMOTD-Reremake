/*
 * Copyright (C) 2019 Bukkit Commons
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.andylizi.colormotdreremake.bukkit;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.UUID;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.injector.GamePhase;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import com.comphenix.protocol.wrappers.WrappedServerPing.CompressedImage;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.*;
import net.andylizi.colormotdreremake.common.CommonUtil;
import net.andylizi.colormotdreremake.common.Config;
import net.andylizi.colormotdreremake.common.Firewall;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.*;

public class MotdListener implements PacketListener {
    @Getter
    private final BukkitMain plugin;
    @Getter
    private final ListeningWhitelist sendingWhitelist;
    @Getter
    private final ListeningWhitelist receivingWhitelist;
    @NotNull
    private final LoadingCache<RenderedImage, CompressedImage> faviconCache = CacheBuilder.newBuilder()
            .weakKeys()
            .build(CacheLoader.from(MotdListener::_fromPng));

    public MotdListener(@NotNull BukkitMain plugin) {
        this.plugin = plugin;
        this.receivingWhitelist = ListeningWhitelist.newBuilder()
                .gamePhase(GamePhase.LOGIN)
                .priority(ListenerPriority.LOW)
                .types(PacketType.Status.Client.PING)  // TODO: support IN_PING in old versions
                .options(new ListenerOptions[]{ ListenerOptions.ASYNC })
                .build();
        this.sendingWhitelist = ListeningWhitelist.newBuilder()
                .gamePhase(GamePhase.LOGIN)
                .priority(ListenerPriority.NORMAL)
                .types(PacketType.Status.Server.SERVER_INFO)
                .options(new ListenerOptions[]{ ListenerOptions.ASYNC })
                .build();
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        if (event.isCancelled()) return;
        String ip = event.getPlayer().getAddress().getHostString();

        //Firewall check
        if (!plugin.getFirewall().canFlushMotd(ip)) {
            event.setCancelled(true);
            return;
        }
        WrappedServerPing status = null;
        try {
            status = event.getPacket().getServerPings().getValues().get(0);
        } catch (IndexOutOfBoundsException ex) {
            return;
        }

        Config config = plugin.config();
        String motd = config.isMaintenanceMode() ? plugin.getBukkitPlaceHolder().applyPlaceHolder(config.getMaintenanceModeMotd(),ip) : config.randomMotd();
        BufferedImage favicon = plugin.favicons().chooseFavicon(config.isMaintenanceMode());

        status.setMotD(WrappedChatComponent.fromText(plugin.getBukkitPlaceHolder().applyPlaceHolder(motd, ip)));
        status.setFavicon(favicon == null ? null : faviconCache.getUnchecked(favicon));

        if (!config.isShowPing()) {
            String onlineMsg = plugin.getBukkitPlaceHolder().applyPlaceHolder(config.randomOnlineMsg(),ip);
            if (onlineMsg != null) {
                status.setVersionProtocol(-1);
                status.setVersionName(ChatColor.translateAlternateColorCodes('&', onlineMsg));
            }
        }

        List<String> players = config.getPlayers();
        if (players != null && !players.isEmpty()) {
            status.setPlayers(config.getPlayers().stream()
                    .map(line -> plugin.getBukkitPlaceHolder().applyPlaceHolder(line,ip))
                    .map(line -> new WrappedGameProfile(UUID.randomUUID(), line))
                    .collect(CommonUtil.toImmutableList()));
            status.setPlayersVisible(true);
        }

        event.getPacket().getServerPings().getValues().set(0, status);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        if (!plugin.config().isShowPing()) {
            event.setCancelled(true);
        }
        if(plugin.getFirewall().isBlocked(event.getPlayer().getAddress().getHostString(),false)) {
            event.setCancelled(true);
        }
    }

    private static CompressedImage _fromPng(RenderedImage img) {
        try {
            // Minimize memory footprint
            return CompressedImage.fromEncodedText(CompressedImage.fromPng(img).toEncodedText().intern());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
