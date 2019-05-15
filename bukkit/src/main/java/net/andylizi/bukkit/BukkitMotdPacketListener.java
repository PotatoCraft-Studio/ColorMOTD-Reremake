package net.andylizi.bukkit;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.injector.GamePhase;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;


public class BukkitMotdPacketListener implements PacketListener {
    private ListeningWhitelist sendingWhitelist;
    private ListeningWhitelist receivingWhitelist;
    private JavaPlugin plugin;
    private PacketType packetServerInfo;
    private PacketType pingPacketType;

    public BukkitMotdPacketListener(JavaPlugin plugin){
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
    }

    @Override
    public void onPacketReceiving(PacketEvent packetEvent) {

    }

    @Override
    public void onPacketSending(PacketEvent packetEvent) {

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
