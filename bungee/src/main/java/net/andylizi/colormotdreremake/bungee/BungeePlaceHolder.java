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

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import net.andylizi.colormotdreremake.common.Config;
import net.andylizi.colormotdreremake.common.InternalPlaceHolder;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;

public class BungeePlaceHolder {
    Config config;
    InternalPlaceHolder internalPlaceHolder ;

    public BungeePlaceHolder(Config config){
        this.config = config;
        this.internalPlaceHolder = new InternalPlaceHolder(config);
    }

    public String applyPlaceHolder(String text, String ip){
    text = ChatColor.translateAlternateColorCodes('&', text);
    text = internalPlaceHolder.applyPlaceHolder(text, ip);
    text = text.replaceAll("%online%", String.valueOf(ProxyServer.getInstance().getOnlineCount()));
    text = text.replaceAll("%maxplayer%", String.valueOf(ProxyServer.getInstance().getConfig().getPlayerLimit()));
    text = text.replaceAll("%date%", getDate());
    text = text.replaceAll("%time%", getTime());

    return text;
    }
    public String getDate(){
        Date now = new Date();
        DateFormat d1 = DateFormat.getDateInstance(DateFormat.MEDIUM);
        return d1.format(now);
    }
    public String getTime(){
        Date now = new Date();
        DateFormat d1 = DateFormat.getTimeInstance(DateFormat.MEDIUM);
        return d1.format(now);
    }

}
