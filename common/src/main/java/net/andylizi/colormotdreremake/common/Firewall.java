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
package net.andylizi.colormotdreremake.common;

import java.util.HashMap;
import java.util.Map;

public class Firewall {
    private final Map<String,Integer> firewallMap = new HashMap<>();
    private final Config config;
    private long lastClear = System.currentTimeMillis();

    public Firewall(Config config){
        this.config = config;
    }

    public boolean canFlushMotd(String ip){
        if(lastClear > config.getLimitTime()) { //清理列表
            firewallMap.clear();
            lastClear = System.currentTimeMillis();
        }

        if(!firewallMap.containsKey(ip)) {
            firewallMap.put(ip, 1); //初始化变量
            return true;
        }

        //IP已存在
        if(firewallMap.get(ip) > config.getRequestLimit()) //检查是否被屏蔽
            return false;

        firewallMap.put(ip, firewallMap.get(ip)+1);
        return true;
    }
    public boolean isBlocked(String ip){
        if(lastClear > config.getLimitTime()) {
            firewallMap.clear();
            lastClear = System.currentTimeMillis();
        }
        if(!firewallMap.containsKey(ip))
            return false;
        if(firewallMap.get(ip) > config.getRequestLimit())
            return true;
        return false;
    }
}
