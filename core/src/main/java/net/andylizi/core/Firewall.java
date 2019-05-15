package net.andylizi.core;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class Firewall {
    @Getter
    private Map<String, Integer> requestMap = new HashMap<>(); //防御记录器
    private Config config;
    private long lastUnbanIpTime = 0;
    public Firewall(Config config){
        this.config=config;
    }

    public boolean canFlushMotd(String ip){
        if(config.isUnderAttack())
            return false;
        int flushedCount = 1;
        if(!requestMap.containsKey(ip)){
            requestMap.put(ip, flushedCount);
        }else{
            flushedCount = requestMap.get(ip);
            flushedCount++;
            requestMap.put(ip, flushedCount);
        }

        return true;
    }

    public boolean isBlocked(String ip){
        if((System.currentTimeMillis() - lastUnbanIpTime) > 120000)
            requestMap.clear();
        if(requestMap.get(ip)>config.getRequestLimit())
            return true;
        return false;
    }
}
