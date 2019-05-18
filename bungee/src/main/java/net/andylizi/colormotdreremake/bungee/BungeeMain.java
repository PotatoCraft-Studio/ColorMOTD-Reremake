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
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import org.bstats.bungeecord.Metrics;

@Getter
public final class BungeeMain extends Plugin {
    private ConfigManager configManager;
    private BungeePlaceHolder bungeePlaceHolder;
    private Firewall firewall;
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
        Metrics metrics = new Metrics(this);
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
