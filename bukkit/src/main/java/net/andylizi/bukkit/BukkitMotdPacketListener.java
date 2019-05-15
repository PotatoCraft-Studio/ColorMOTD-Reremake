package net.andylizi.bukkit;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.injector.GamePhase;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import com.google.common.collect.ImmutableList;
import lombok.Getter;
import net.andylizi.core.Firewall;
import org.bukkit.plugin.Plugin;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class BukkitMotdPacketListener implements PacketListener {
    private ListeningWhitelist sendingWhitelist;
    private ListeningWhitelist receivingWhitelist;
    private BukkitMain plugin;
    private PacketType packetServerInfo;
    private PacketType pingPacketType;
    @Getter  private Firewall firewall;


    public BukkitMotdPacketListener(BukkitMain plugin){
        this.plugin = plugin;
        try{
            packetServerInfo = ReflectFactory.getServerInfoPacketType();
            pingPacketType = ReflectFactory.getPingPacketType();
        }catch (NoSuchFieldException e){
            e.printStackTrace();
        }
        if(packetServerInfo==null) {
            plugin.getLogger().severe("请升级ProtocolLib版本!");
            plugin.getPluginLoader().disablePlugin(plugin);
            return;
        }

        this.sendingWhitelist = ListeningWhitelist.newBuilder()
                .priority(ListenerPriority.HIGH)
                .types(packetServerInfo)
                .gamePhase(GamePhase.LOGIN)
                .options(new ListenerOptions[]{ListenerOptions.ASYNC})
                .build();
        this.receivingWhitelist = ListeningWhitelist.newBuilder()
                .priority(ListenerPriority.HIGH)
                .types(pingPacketType)
                .gamePhase(GamePhase.LOGIN)
                .options(new ListenerOptions[]{ListenerOptions.ASYNC})
                .build();
        firewall = new Firewall(this.plugin.getPluginConfig());
    }

    @Override
    public void onPacketReceiving(PacketEvent packetEvent) {
        String ip = packetEvent.getPlayer().getAddress().getHostString();
        if(firewall.isBlocked(ip)) { //已被屏蔽IP地址直接不予回复
            packetEvent.setCancelled(true);
            return;
        }
        if(!plugin.getPluginConfig().isShowDelay()){
            packetEvent.setCancelled(true);
            return;
        }
    }

    @Override
    public void onPacketSending(PacketEvent packetEvent) {
        String ip = packetEvent.getPlayer().getAddress().getHostString();
        if(!firewall.canFlushMotd(ip)) //防火墙防刷MOTD检查
            packetEvent.setCancelled(true);

        if (!packetEvent.getPacketType().equals(packetServerInfo))
            return;

        //MOTD Start

        WrappedServerPing ping = null; //数据包检查
        try {
            ping = packetEvent.getPacket().getServerPings().getValues().get(0);
        } catch (IndexOutOfBoundsException ex) {
            return;
        }

        String randomMotd;
        BufferedImage randomIcon;
        if(!plugin.getPluginConfig().isInMaintenance()){//检查是否为维护状态
            //正常模式
            randomMotd = this.plugin.getPluginConfig().randomMotd();
            randomIcon = this.plugin.getIcon().randomIcon();
        }else{
            //维护模式 固定MOTD/ICON
            randomMotd = this.plugin.getPluginConfig().getMaintenanceModeMotd();
            randomIcon = this.plugin.getIcon().getMaintenanceImage();
        }
        //格式化文本与图像
        String motd = this.plugin.getFormatter().applyPlaceHolder(randomMotd, ip);
        WrappedServerPing.CompressedImage icon;
        try {
            icon = WrappedServerPing.CompressedImage.fromPng(randomIcon);
        }catch (IOException ignored){icon = null;}

        //设置playerlist
        if(!plugin.getPluginConfig().isShowDelay()){ //开启showdelay时，无法发送自定义在线信息
            ping.setVersionProtocol(-1);
            ping.setVersionName(this.plugin.getFormatter().applyPlaceHolder(plugin.getPluginConfig().randomOnline(), ip));
        }

        if(!plugin.getPluginConfig().getPlayers().isEmpty()){
            List<WrappedGameProfile> profileList = new ArrayList<>();
            for (String str : plugin.getPluginConfig().getPlayers()) {
                profileList.add(createGameProfile(this.plugin.getFormatter().applyPlaceHolder(str, ip)));
            }
            ping.setPlayers(ImmutableList.copyOf(profileList));
            ping.setPlayersVisible(true);
        }
        //应用
        ping.setMotD(motd);
        ping.setFavicon(icon);
        packetEvent.getPacket().getServerPings().getValues().set(0, ping);
    }

    @Override
    public ListeningWhitelist getReceivingWhitelist() {
        return receivingWhitelist;
    }

    @Override
    public ListeningWhitelist getSendingWhitelist() {
        return sendingWhitelist;
    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }
    public static WrappedGameProfile createGameProfile(String name) {
        return new WrappedGameProfile(UUID.nameUUIDFromBytes(name.getBytes(StandardCharsets.UTF_8)), name);
    }
}
