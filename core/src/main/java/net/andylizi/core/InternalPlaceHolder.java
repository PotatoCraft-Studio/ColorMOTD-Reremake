package net.andylizi.core;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class InternalPlaceHolder implements PlaceHolder {
    Map<String,TaobaoReturnResult> cacheMap = new HashMap<>();
    public String applyPlaceHolder(String text){
        text = text.replaceAll("%date%",getDate());
        text = text.replaceAll("%time%",getTime());
      return text;
    }
    public String getDate(){
        Calendar cal=Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return year+"/"+month+"/"+day;

    }
    public String getTime(){
        Calendar cal=Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        return hour+":"+minute+":"+second;
    }
    public String formatColor(String unformatcoloredText){
        unformatcoloredText = unformatcoloredText.replaceAll("&&", "{&&}"); //把不想转换为颜色符号的&转换
        unformatcoloredText = unformatcoloredText.replaceAll("&", "§");
        unformatcoloredText = unformatcoloredText.replaceAll("{&&}","&" ); //转回去
        return unformatcoloredText;
    }
    public TaobaoReturnResult getIpInfomation(String ip){
        String json = HttpUtils.sendGet("http://ip.taobao.com/service/getIpInfo.php","ip="+ip);
        if(json.isEmpty())
            return null;
        Gson gson = new Gson();
        try{
            return gson.fromJson(json, TaobaoReturnResult.class);
        }catch (JsonSyntaxException jse){
            return null;
        }
    }
}
