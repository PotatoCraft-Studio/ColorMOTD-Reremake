
import com.google.common.collect.Lists;
import lombok.Getter;
import net.andylizi.core.Firewall;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.awt.image.BufferedImage;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class BungeeMotdListener implements Listener {
    BungeeMain plugin;
    @Getter Firewall firewall;
    public BungeeMotdListener(BungeeMain plugin){
        this.plugin=plugin;
         firewall = new Firewall(plugin.getPluginConfig());
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onServerListPing(ProxyPingEvent event){
        String ip = event.getConnection().getAddress().getHostString();

        //防火墙检查
        if(!firewall.canFlushMotd(ip)) {
            event.setResponse(new ServerPing());
            return;
        }

        final ListenerInfo listenerInfo = event.getConnection().getListener();
        ServerPing response = event.getResponse() == null ? new ServerPing() : event.getResponse();
        BufferedImage randomIcon;
        String randomMotd;

        // 生成 MOTD 与 Favicon
        if(!plugin.getPluginConfig().isInMaintenance()){ //检查是否为维护模式
            randomMotd = plugin.getFormatter().applyPlaceHolder(plugin.getPluginConfig().randomMotd(), ip);
            randomIcon = plugin.getIcon().randomIcon();
        }else{
            randomMotd = plugin.getFormatter().applyPlaceHolder(plugin.getPluginConfig().getMaintenanceModeMotd(), ip);
            randomIcon = plugin.getIcon().getMaintenanceImage();
        }

        // 检查是否关闭延迟检测
        if(!plugin.getPluginConfig().isShowDelay()){
            plugin.getProxy().getPluginConfig().getPlayerLimit();
            response.setVersion(new ServerPing.Protocol(plugin.getFormatter().applyPlaceHolder(plugin.getPluginConfig().randomOnline(), ip), -1));
        }

        // 设置playerlist
        if(!plugin.getPluginConfig().getPlayers().isEmpty()){
            ServerPing.PlayerInfo[] playerInfos = new ServerPing.PlayerInfo[0];
            int i = 0;
            for (String text : plugin.getPluginConfig().getPlayers()){
                ServerPing.PlayerInfo info = new ServerPing.PlayerInfo(text, UUID.nameUUIDFromBytes(text.getBytes(StandardCharsets.UTF_8)));
                playerInfos[i] = info;
                i++;
            }
            response.setPlayers(new ServerPing.Players(0, ProxyServer.getInstance().getOnlineCount(), playerInfos));
        }

        // 设置 Icon 与 Motd
        Favicon icon = Favicon.create(randomIcon);
        response.setDescriptionComponent((new TextComponent(plugin.getFormatter().applyPlaceHolder(randomMotd, ip))));
        event.setResponse(response);
    }
}
