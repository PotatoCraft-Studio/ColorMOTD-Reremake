package net.andylizi.core;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import lombok.Getter;
import lombok.Setter;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class InternalPlaceHolder implements PlaceHolder {
    @Getter @Setter
    Map<String,TaobaoReturnResult> cacheMap = new HashMap<>();
    private final static int cacheSize = 100;

    public InternalPlaceHolder(){

    }
    public String applyPlaceHolder(String text,String ip){
        text = formatColor(text);
        text = text.replaceAll("%date%",getDate());
        text = text.replaceAll("%time%",getTime());
        TaobaoReturnResult ipResult = this.getIpInfomation(ip); //Yes, this contains caches.
        TaobaoJsonData ipData = ipResult.getData();
        text = text.replaceAll("%loc%", ipData.getCountry()+ipData.getRegion()+ipData.getCity());
        text = text.replaceAll("%isp%", ipData.getIsp());
        text = text.replaceAll("%ip%",ipData.getIp());
        text = text.replaceAll("%country%", ipData.getCountry());
        text = text.replaceAll("%region%", ipData.getRegion());
        text = text.replaceAll("%city%", ipData.getCity());
        text = text.replaceAll("%random%", String.valueOf((new Random()).nextInt(10)));
      return text;
    }
    private String getDate(){
        Calendar cal=Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return year+"/"+month+"/"+day;

    }
    private String getTime(){
        Calendar cal=Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        return hour+":"+minute+":"+second;
    }
    public String formatColor(String unformatcoloredText){
        unformatcoloredText = unformatcoloredText.replaceAll("&&", "###&&###"); //把不想转换为颜色符号的&转换
        unformatcoloredText = unformatcoloredText.replaceAll("&", "§");
        unformatcoloredText = unformatcoloredText.replaceAll("###&&###","&" ); //转回去
        return unformatcoloredText;
    }
    public TaobaoReturnResult getIpInfomation(String ip){
        String json = HttpUtils.sendGet("http://ip.taobao.com/service/getIpInfo.php","ip="+ip);
        if(json.isEmpty())
            return null;
        Gson gson = new Gson();
        try{
            TaobaoReturnResult taobaoReturnResult = gson.fromJson(json, TaobaoReturnResult.class);
            if(!cacheMap.containsKey(ip)) {
                cacheMap.put(ip, taobaoReturnResult);
                if(cacheMap.size() > cacheSize)
                    cacheMap.clear();
            }
            return taobaoReturnResult;
        }catch (JsonSyntaxException jse){
            return null;
        }
    }
}
