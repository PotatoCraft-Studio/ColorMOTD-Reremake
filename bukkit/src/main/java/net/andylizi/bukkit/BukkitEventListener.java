package net.andylizi.bukkit;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class BukkitEventListener implements Listener {

    BukkitMain plugin;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerLoginEvent e) {
        if (!plugin.getPluginConfig().isInMaintenance())
            return;
        if (e.getPlayer().isOp() || e.getPlayer().hasPermission("colormotd.smode.join")) {
            e.allow();
        } else {
            e.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, plugin.getFormatter().applyPlaceHolder(plugin.getPluginConfig().getMaintenanceModeKickMsg(), e.getAddress().getHostAddress()));
        }
    }
}
