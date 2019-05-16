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

import lombok.*;

public class InternalPlaceHolder {
    Config config;
    IpAddressManager ipAddressManager;
    public InternalPlaceHolder(Config config){
        this.config = config;
        ipAddressManager = new IpAddressManager(config);
    }
    public String applyPlaceHolder(String text, String ip){
        //颜色处理需要各自的PlaceHolder里完成，内部类只负责处理变量
        IpAddressInfo ipAddressInfo = ipAddressManager.getIpInfomations(ip);
        text = text.replaceAll("%ip%", ipAddressInfo.getIp());
        text = text.replaceAll("%country%", ipAddressInfo.getCountry());
        text = text.replaceAll("%region%" , ipAddressInfo.getRegion());
        text = text.replaceAll("%area%" , ipAddressInfo.getArea());
        text = text.replaceAll("%isp%", ipAddressInfo.getIsp());
        text = text.replaceAll("%timestamp%", String.valueOf(System.currentTimeMillis()));
        return text;
    }

}
