package net.andylizi.core;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class Firewall {
    @Getter
    private Map<String, Integer> requestMap = new HashMap<>(); //防御记录器
    private Config config;
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
        if(requestMap.get(ip)>config.getRequestLimit())
            return true;
        return false;
    }
}
