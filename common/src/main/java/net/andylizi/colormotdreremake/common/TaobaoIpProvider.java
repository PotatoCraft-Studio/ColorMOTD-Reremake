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

@Getter
@Setter
@Builder
public class TaobaoIpProvider implements IpAddressInfo {
    int code;
    TaobaoIpData data;

    @Override
    public String getIp() {
        return data.getIp();
    }

    @Override
    public String getCountry() {
        return data.getCountry();
    }

    @Override
    public String getRegion() {
        return data.getRegion();
    }

    @Override
    public String getArea() {
        return data.getArea();
    }

    @Override
    public String getIsp() {
        return data.getIsp();
    }
    @Getter
    @Setter
    @Builder
    static class TaobaoIpData{
        private String ip;
        private String country;
        private String area;
        private String region;
        private String city;
        private String county;
        private String isp;
        private String country_id;
        private String area_id;
        private String region_id;
        private String city_id;
        private String county_id;
        private String isp_id;
    }
}

