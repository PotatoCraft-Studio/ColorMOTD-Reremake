package net.andylizi.core;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RequiredArgsConstructor(staticName = "of")
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Config {
    @Getter
    @Setter
    private List<String> motds;
    @Getter
    @Setter
    private List<String> onlineMsgs;
    @Getter
    @Setter
    private List<String> players;
    @Getter
    @Setter
    private String maintenanceModeMotd;
    @Getter
    @Setter
    private String maintenanceModeKickMsg;
    //@Getter @Setter private String ipService;
    @Getter
    @Setter
    private String tpsFormat;
    @Getter
    @Setter
    private boolean showDelay;
    @Getter
    @Setter
    private boolean inMaintenance;
    @Getter
    @Setter
    private boolean underAttack;
    @Getter
    @Setter
    private int requestLimit;
    @Getter
    @Setter
    private boolean usePlaceHolderAPI;

    public static Config createDefaultConfig() {
        List<String> motds = new ArrayList<>();
        motds.add("&b欢迎来到我们服务器~这是第&e1&b条随机消息\\n&d现在时间: &e%DATE% %TIME%");
        motds.add("&b欢迎来到我们服务器~这是第&e2&b条随机消息\\n&d在线人数: &e%ONLINE%");
        motds.add("&b欢迎来到我们服务器~这是第&e3&b条随机消息\\n&d欢迎来自&e%LOC%&d的%ISP%&d玩家");
        motds.add("&b欢迎来到我们服务器~这是第&e4&b条随机消息\\n&d欢迎来自&e%LOC%&d的%ISP%&d玩家");
        motds.add("&b欢迎来到我们服务器~这是第&e5&b条随机消息\\n&d现在时间: &e%DATE% %TIME%");
        motds.add("&b欢迎来到我们服务器~这是第&e6&b条随机消息\\n&d在线人数: &e%ONLINE%");
        List<String> onlineMsgs = new ArrayList<>();
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
        boolean underAttack = false;
        int requestLimit = 15;
        boolean usePlaceHolderAPI = true;
        return new Config(motds, onlineMsgs, players, maintenanceModeMotd,
                maintenanceModeKickMsg, tpsFormat, showDelay,
                inMaintenance, underAttack, requestLimit, usePlaceHolderAPI);
    }
    public String randomMotd(){
        Random random = new Random();
        return motds.get(random.nextInt(motds.size()));
    }
    public String randomOnline(){
        Random random = new Random();
        return onlineMsgs.get(random.nextInt(onlineMsgs.size()));
    }
}
