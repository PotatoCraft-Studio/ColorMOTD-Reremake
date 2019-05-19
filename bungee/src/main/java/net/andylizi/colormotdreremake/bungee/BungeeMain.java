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
package net.andylizi.colormotdreremake.bungee;

import lombok.*;
import net.andylizi.colormotdreremake.common.Config;
import net.andylizi.colormotdreremake.common.ConfigManager;
import net.andylizi.colormotdreremake.common.FaviconList;
import net.andylizi.colormotdreremake.common.Firewall;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

@Getter
public final class BungeeMain extends Plugin {
    private ConfigManager configManager;
    private BungeePlaceHolder bungeePlaceHolder;
    private Firewall firewall;
    final String prefix = ChatColor.AQUA+"["+ChatColor.GOLD+"ColorMOTD-Bungee"+ChatColor.AQUA+"] "+ChatColor.GREEN;
    @Override
    public void onEnable() {
        this.configManager = new ConfigManager(getDataFolder().toPath());
        this.configManager.saveDefaultResources();
        this.configManager.loadConfig();
        this.configManager.loadFavicons();

        bungeePlaceHolder = new BungeePlaceHolder(config());
        firewall = new Firewall(config());

        PluginManager pm = getProxy().getPluginManager();
        pm.registerListener(this, new MotdListener(this));
        pm.registerListener(this, new LoginListener(this));
        MetricsLite metrics = new MetricsLite(this);
        Command cmdMain = new Command("colormotd","colormotd.admin","") {
            @Override
            public void execute(CommandSender sender, String[] strings) {
                if(strings.length<1){
                    sender.sendMessage(new TextComponent(prefix+"支持的子命令: smode emode reload"));
                }
                if(!sender.hasPermission("colormotd.admin")){
                    sender.sendMessage(new TextComponent(prefix+"权限不足"));
                }
                if(strings[0].equals("smode")){
                    if(config().isMaintenanceMode()){
                        config().setMaintenanceMode(false);
                        getConfigManager().saveConfig();
                        sender.sendMessage(new TextComponent(prefix+"维护模式已关闭"));
                    }else{
                        config().setMaintenanceMode(true);
                        getConfigManager().saveConfig();
                        for (ProxiedPlayer onlinePlayer : getProxy().getPlayers()){
                            if(!onlinePlayer.hasPermission("colormotd.maintenance.join"))
                                onlinePlayer.disconnect(new TextComponent(getBungeePlaceHolder().applyPlaceHolder(config().getMaintenanceModeKickMsg(), onlinePlayer.getAddress().getHostString())));
                        }
                        sender.sendMessage(new TextComponent(prefix+"维护模式已开启，所有非OP和无colormotd.maintenance.join权限玩家已被移出服务器并在关闭前无法加入服务器"));
                    }
                }

                if(strings[0].equals("emode")) {
                    if (config().isEmergencyMode()) {
                        config().setEmergencyMode(false);
                        getConfigManager().saveConfig();
                        sender.sendMessage(new TextComponent(prefix + "应急模式已关闭"));
                    } else {
                        config().setEmergencyMode(true);
                        getConfigManager().saveConfig();
                        sender.sendMessage(new TextComponent(prefix + "应急模式已开启，现在将忽略所有MOTD请求"));
                    }
                }
                sender.sendMessage(new TextComponent(prefix+"支持的子命令: smode emode reload"));
            }
        };
        Command cmdSmode = new Command("smode") {
            @Override
            public void execute(CommandSender sender, String[] strings) {
                getProxy().getPluginManager().dispatchCommand(sender, "colormotd smode");
            }
        };
        Command cmdEmode = new Command("smode") {
            @Override
            public void execute(CommandSender sender, String[] strings) {
                getProxy().getPluginManager().dispatchCommand(sender, "colormotd emode");
            }
        };
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
}
