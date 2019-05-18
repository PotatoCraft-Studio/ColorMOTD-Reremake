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

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import lombok.*;
import net.andylizi.colormotdreremake.common.Config;
import net.andylizi.colormotdreremake.common.ConfigManager;
import net.andylizi.colormotdreremake.common.FaviconList;
import net.andylizi.colormotdreremake.common.Firewall;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
@Getter
public final class BukkitMain extends JavaPlugin {
    private ConfigManager configManager;
    private ProtocolManager protocolManager;
    private BukkitPlaceHolder bukkitPlaceHolder;
    public static boolean usePlaceHolderAPI;
    private Firewall firewall;
    final String prefix = ChatColor.AQUA+"["+ChatColor.GOLD+"ColorMOTD-Bukkit"+ChatColor.AQUA+"] "+ChatColor.GREEN;
    @Override
    public void onEnable() {
        PluginManager pm = Bukkit.getPluginManager();
        if (!pm.isPluginEnabled("ProtocolLib") ||
                (protocolManager = ProtocolLibrary.getProtocolManager()) == null || protocolManager.isClosed()) {
            getLogger().warning("Unable to load ProtocolLib!");
            this.getPluginLoader().disablePlugin(this);
            return;
        }

        getLogger().info("Loaded " + ProtocolLibrary.getPlugin().getDescription().getFullName());

        this.configManager = new ConfigManager(getDataFolder().toPath());
        this.configManager.saveDefaultResources();
        this.configManager.loadConfig();
        this.configManager.loadFavicons();
        firewall = new Firewall(config());
        bukkitPlaceHolder = new BukkitPlaceHolder(config());

        this.protocolManager.addPacketListener(new MotdListener(this));
        pm.registerEvents(new LoginListener(this), this);
        if(Bukkit.getPluginManager().getPlugin("PlaceHolderAPI")!=null && config().isUsePlaceHolderAPI())
            usePlaceHolderAPI = true;
        Metrics metrics = new Metrics(this);
    }

    @Override
    public void onDisable() {
        if (protocolManager != null) {
            protocolManager.removePacketListeners(this);
        }
    }

    public ConfigManager configManager() {
        return configManager;
    }

    public Config config() {
        return configManager().getConfig();
    }

    public FaviconList favicons() {
        return configManager().getFavicons();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equals("smode")){
            Bukkit.dispatchCommand(sender, "colormotd smode");
            return true;
        }
        if(command.getName().equals("emode")){
            Bukkit.dispatchCommand(sender, "colormotd emode");
            return true;
        }
        if(!command.getName().equals("colormotd"))
            return false;
        if(sender.hasPermission("colormotd.admin")) {
            sender.sendMessage(prefix + "权限不足");
            return true;
        }
        if(args.length<1){
            sender.sendMessage(prefix+"支持的子命令: smode emode reload");
            return true;
        }

        if(args[0].equals("reload")){
            getConfigManager().loadConfig();
            sender.sendMessage(prefix+"重载完成");
            return true;
        }
        if(args[0].equals("smode")){
            if(config().isMaintenanceMode()){
                config().setMaintenanceMode(false);
                getConfigManager().saveConfig();
                sender.sendMessage(prefix+"维护模式已关闭");
            }else{
                config().setMaintenanceMode(true);
                getConfigManager().saveConfig();
                for (Player onlinePlayer : ReflectFactory.getPlayers()){
                    if(!onlinePlayer.isOp() && !onlinePlayer.hasPermission("colormotd.maintenance.join"))
                        onlinePlayer.kickPlayer(getBukkitPlaceHolder().applyPlaceHolder(config().getMaintenanceModeKickMsg(), onlinePlayer.getAddress().getHostString()));
                }
                sender.sendMessage(prefix+"维护模式已开启，所有非OP和无colormotd.maintenance.join权限玩家已被移出服务器并在关闭前无法加入服务器");
            }
            return true;
        }
        if(args[0].equals("emode")) {
            if (config().isEmergencyMode()) {
                config().setEmergencyMode(false);
                getConfigManager().saveConfig();
                sender.sendMessage(prefix + "应急模式已关闭");
            } else {
                config().setEmergencyMode(true);
                getConfigManager().saveConfig();
                sender.sendMessage(prefix + "应急模式已开启，现在将忽略所有MOTD请求");
            }
            return true;
        }
        sender.sendMessage(prefix+"支持的子命令: smode emode reload");
        return true;
    }
}
