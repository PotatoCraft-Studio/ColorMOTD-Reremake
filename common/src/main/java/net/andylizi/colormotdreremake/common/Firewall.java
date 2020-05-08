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
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.jetbrains.annotations.*;

public class Firewall {
        private final Cache<String, Integer> cache;
    private final Config config;

    public Firewall(Config config){
        this.config = config;
        this.cache = CacheBuilder.newBuilder()
                .expireAfterWrite(config.getLimitTime(), TimeUnit.MILLISECONDS)
        .build();
    }

    public boolean canFlushMotd(@NotNull String ip){
        return !isBlocked(ip, true);
    }
    public boolean isBlocked(@NotNull String ip, boolean plus){
        Integer count = cache.getIfPresent(ip);
        if(count == null){
            count = 0;
        }
        boolean result = count <= config.getRequestLimit();
        if(plus){
            cache.put(ip, ++count);
        }
        return result;
    }
}
