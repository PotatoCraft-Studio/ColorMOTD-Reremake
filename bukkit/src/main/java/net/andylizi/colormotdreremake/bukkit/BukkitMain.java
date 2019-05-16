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
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
@Getter
public final class BukkitMain extends JavaPlugin {
    private ConfigManager configManager;
    private ProtocolManager protocolManager;
    private BukkitPlaceHolder bukkitPlaceHolder;
    public static boolean usePlaceHolderAPI;
    private Firewall firewall;
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
}
