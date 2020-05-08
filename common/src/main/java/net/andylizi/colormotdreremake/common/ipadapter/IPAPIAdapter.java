/*
 * Copyright (C) 2020 Bukkit Commons
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
package net.andylizi.colormotdreremake.common.ipadapter;

import java.io.IOException;
import java.net.URL;

import com.google.gson.Gson;
import net.andylizi.colormotdreremake.common.HttpRequest;
import org.jetbrains.annotations.*;

public class IPAPIAdapter implements IpAdapter {
    private Gson gson = new Gson();

    @Override
    public @NotNull IpResult search(@NotNull String ip) {
        String remoteData = null;
        try {
            remoteData = HttpRequest.get(new URL("http://ip-api.com/json/" + ip + "?lang=zh-CN"))
                    .execute(5000)
                    .returnContent()
                    .asString("UTF-8")
                    .trim();


        IPAPIReturns returns = gson.fromJson(remoteData, IPAPIReturns.class);

        String country = returns.getCountry();
        String region = returns.getRegion();
        String isp = returns.getIsp();
        if (country == null) {country = "未知";}
        if (region == null) {region = "未知";}
        if (isp == null) {isp = "未知";}
        return new IpResult(ip, country, region, isp);
        } catch (IOException e) {
            return new IpResult(ip, "未知", "未知", "未知");
        }

    }

    static class IPAPIReturns {

        /**
         * zip : H1S
         * country : 加拿大
         * city : 蒙特利尔
         * org : Videotron Ltee
         * timezone : America/Toronto
         * regionName : Quebec
         * isp : Le Groupe Videotron Ltee
         * query : 24.48.0.1
         * lon : -73.5825
         * as : AS5769 Videotron Telecom Ltee
         * countryCode : CA
         * region : QC
         * lat : 45.5808
         * status : success
         */
        private String zip;
        private String country;
        private String city;
        private String org;
        private String timezone;
        private String regionName;
        private String isp;
        private String query;
        private double lon;
        private String as;
        private String countryCode;
        private String region;
        private double lat;
        private String status;

        public void setZip(String zip) { this.zip = zip;}

        public void setCountry(String country) { this.country = country;}

        public void setCity(String city) { this.city = city;}

        public void setOrg(String org) { this.org = org;}

        public void setTimezone(String timezone) { this.timezone = timezone;}

        public void setRegionName(String regionName) { this.regionName = regionName;}

        public void setIsp(String isp) { this.isp = isp;}

        public void setQuery(String query) { this.query = query;}

        public void setLon(double lon) { this.lon = lon;}

        public void setAs(String as) { this.as = as;}

        public void setCountryCode(String countryCode) { this.countryCode = countryCode;}

        public void setRegion(String region) { this.region = region;}

        public void setLat(double lat) { this.lat = lat;}

        public void setStatus(String status) { this.status = status;}

        public String getZip() { return zip;}

        public String getCountry() { return country;}

        public String getCity() { return city;}

        public String getOrg() { return org;}

        public String getTimezone() { return timezone;}

        public String getRegionName() { return regionName;}

        public String getIsp() { return isp;}

        public String getQuery() { return query;}

        public double getLon() { return lon;}

        public String getAs() { return as;}

        public String getCountryCode() { return countryCode;}

        public String getRegion() { return region;}

        public double getLat() { return lat;}

        public String getStatus() { return status;}
    }
}
