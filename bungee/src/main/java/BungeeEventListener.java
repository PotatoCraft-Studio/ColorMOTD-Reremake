import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class BungeeEventListener implements Listener {
    BungeeMain plugin;
    public BungeeEventListener(BungeeMain plugin){
        this.plugin = plugin;
    }
    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PostLoginEvent event){
        String ip = event.getPlayer().getAddress().getHostString();
        if(!plugin.getPluginConfig().isInMaintenance())
            return;
        if(event.getPlayer().hasPermission("colormotd.smode.join"))
            return;
        event.getPlayer().disconnect(new TextComponent(plugin.getFormatter().applyPlaceHolder(plugin.getPluginConfig().getMaintenanceModeKickMsg(), ip)));
        return;
    }

}
