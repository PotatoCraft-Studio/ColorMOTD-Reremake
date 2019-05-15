package net.andylizi.core;

import lombok.Getter;

public class TaobaoReturnResult {
    @Getter private int code;
    @Getter private TaobaoJsonData data;
}
class TaobaoJsonData{
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
