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

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import lombok.*;
@Getter
@Setter
public class IpAddressManager {
    private HashMap<String,IpAddressInfo> cacheMap = new HashMap<>();
    private Config config;
    private Gson gson = new Gson();
    final String taobaoProvider = "http://ip.taobao.com/service/getIpInfo.php?ip=";


    public IpAddressManager(Config config){
        this.config = config;
    }

    public IpAddressInfo getIpInfomations(String ip){
        if(cacheMap.containsKey(ip))
            return cacheMap.get(ip); //返回缓存数据
        IpAddressInfo info = null;
        switch (config.getIpProvider()){
            case "taobao":
                try{
                    info = gson.fromJson(HttpUtils.doGet(taobaoProvider+ip), TaobaoIpProvider.class);
                }catch (JsonSyntaxException ex){
                    //Ignore
                }
                break;
        }
        if(cacheMap.size() > 50)
            cacheMap.clear(); //清空缓存
        cacheMap.put(ip, info); //放置缓存
        return info;
    }
}
