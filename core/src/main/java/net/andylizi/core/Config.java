package net.andylizi.core;


import lombok.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor(staticName = "of")
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Config {
    @Getter @Setter private List<String> motds;
    @Getter @Setter private List<String> onlineMsgs;
    @Getter @Setter private List<String> players;
    @Getter @Setter private String maintenanceModeMotd;
    @Getter @Setter private String maintenanceModeKickMsg;
    //@Getter @Setter private String ipService;
    @Getter @Setter private String tpsFormat;
    @Getter @Setter private boolean showDelay;
    @Getter @Setter private boolean inMaintenance;

    public static Config createDefaultConfig(){
        List<String> motds = new ArrayList<>();
        motds.add("&b欢迎来到我们服务器~这是第&e1&b条随机消息\\n&d现在时间: &e%DATE% %TIME%");
        motds.add("&b欢迎来到我们服务器~这是第&e2&b条随机消息\\n&d在线人数: &e%ONLINE%");
        motds.add("&b欢迎来到我们服务器~这是第&e3&b条随机消息\\n&d欢迎来自&e%LOC%&d的%ISP%&d玩家");
        motds.add("&b欢迎来到我们服务器~这是第&e4&b条随机消息\\n&d欢迎来自&e%LOC%&d的%ISP%&d玩家");
        motds.add("&b欢迎来到我们服务器~这是第&e5&b条随机消息\\n&d现在时间: &e%DATE% %TIME%");
        motds.add("&b欢迎来到我们服务器~这是第&e6&b条随机消息\\n&d在线人数: &e%ONLINE%");
        List<String> onlineMsgs =  new ArrayList<>();
        onlineMsgs.add("&2*&6查看服务器信息&2*      &a在线人数: &b%ONLINE%&d/&2%MAXPLAYER%");
        onlineMsgs.add("&2*&6查看服务器信息&2*      &a现在时间: &e%DATE% %TIME%");
        List<String> players = new ArrayList<>();
        players.add("&b啦啦啦&c啦啦啦&d啦啦啦");
        players.add("&b啦啦啦&c啦啦啦&d啦啦啦");
        players.add("&b啦啦啦&c啦啦啦&d啦啦啦");
        players.add("&b啦啦啦&c啦啦啦&d啦啦啦");
        players.add("&b啦啦啦&c啦啦啦&d啦啦啦");
        String maintenanceModeMotd = "&c服务器维护中,请等待维护完成...";
        String maintenanceModeKickMsg = "&c服务器维护中,请等待维护完成再进入服务器!";
        //String ipService = "taobao";
        String tpsFormat = "0.0";
        boolean showDelay = false;
        boolean inMaintenance = false;
        return new Config(motds, onlineMsgs,players , maintenanceModeMotd, maintenanceModeKickMsg, tpsFormat ,showDelay ,inMaintenance);
    }
}
