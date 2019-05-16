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

import lombok.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.jetbrains.annotations.*;

@RequiredArgsConstructor
public final class LoginListener implements Listener {
    private final @NotNull BukkitMain plugin;


    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onLogin(PlayerLoginEvent event) {
        if (plugin.config().isMaintenanceMode()) {
            Player player = event.getPlayer();
            if (!player.isOp() && !player.hasPermission("colormotd.maintenance.join")) {
                event.disallow(PlayerLoginEvent.Result.KICK_WHITELIST,
                        plugin.getBukkitPlaceHolder().applyPlaceHolder(plugin.config().getMaintenanceModeKickMsg(), event.getAddress().getHostAddress()));
            }
        }
    }
}
