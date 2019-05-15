package net.andylizi.bukkit;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.injector.GamePhase;
import net.andylizi.core.Firewall;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;


public class BukkitMotdPacketListener implements PacketListener {
    private ListeningWhitelist sendingWhitelist;
    private ListeningWhitelist receivingWhitelist;
    private BukkitMain plugin;
    private PacketType packetServerInfo;
    private PacketType pingPacketType;
    private Firewall firewall;


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
        firewall = new Firewall(this.plugin.getConfig());
    }

    @Override
    public void onPacketReceiving(PacketEvent packetEvent) {
        String ip = packetEvent.getPlayer().getAddress().getHostString();
        if(firewall.isBlocked(ip)) { //已被屏蔽IP地址直接不予回复
            packetEvent.setCancelled(true);
            return;
        }
        if(!plugin.getConfig().isShowDelay()){
            packetEvent.setCancelled(true);
            return;
        }
    }

    @Override
    public void onPacketSending(PacketEvent packetEvent) {
        String ip = packetEvent.getPlayer().getAddress().getHostString();
        if(!firewall.canFlushMotd(ip)) //防火墙防刷MOTD检查
            packetEvent.setCancelled(true);
        //MOTD Start

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
}
