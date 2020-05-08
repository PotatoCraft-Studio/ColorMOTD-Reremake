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
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import lombok.*;
import net.andylizi.colormotdreremake.common.ipadapter.IPAPIAdapter;
import net.andylizi.colormotdreremake.common.ipadapter.IpAdapter;
import net.andylizi.colormotdreremake.common.ipadapter.IpResult;
import org.jetbrains.annotations.*;

@Getter
@Setter
public class IpAddressManager {
    private final Cache<String, IpResult> ipCaching =  CacheBuilder.newBuilder().expireAfterAccess(1, TimeUnit.HOURS)
            .maximumSize(500)
            .build();
    private final Config config;
    private final Gson gson = new Gson();
    private final IpAdapter adapter;


    public IpAddressManager(@NotNull Config config){
        this.config = config;
        switch (config.getIpProvider()){ //暂时仅IPAPI可用
            default:
                adapter = new IPAPIAdapter();
        }
    }

    public IpResult getIpInfomations(@NotNull String ip){
        IpResult info = ipCaching.getIfPresent(ip);
        if(info != null){
            return info;
        }
        info = adapter.search(ip);
        return info;
    }
}
