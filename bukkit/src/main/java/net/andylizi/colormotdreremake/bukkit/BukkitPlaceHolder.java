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

import java.util.UUID;

import me.clip.placeholderapi.PlaceholderAPI;
import net.andylizi.colormotdreremake.common.Config;
import net.andylizi.colormotdreremake.common.InternalPlaceHolder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class BukkitPlaceHolder {
    Config config;
    InternalPlaceHolder internalPlaceHolder ;

    public BukkitPlaceHolder(Config config){
        this.config = config;
        this.internalPlaceHolder = new InternalPlaceHolder(config);
    }

    public String applyPlaceHolder(String text, String ip){
        text = ChatColor.translateAlternateColorCodes('&', text);
        text = internalPlaceHolder.applyPlaceHolder(text, ip);
        text = text.replaceAll("%online%", String.valueOf(ReflectFactory.getPlayers().length));
        text = text.replaceAll("%maxplayer%", String.valueOf(Bukkit.getMaxPlayers()));
        text = text.replaceAll("%playedbefore%", String.valueOf(Bukkit.getOfflinePlayers().length));
        if(BukkitMain.usePlaceHolderAPI)
            text = PlaceholderAPI.setPlaceholders(Bukkit.getOfflinePlayer(UUID.fromString("OfflinePlayer:ColorMOTD")), text);
        return text;

    }
}
