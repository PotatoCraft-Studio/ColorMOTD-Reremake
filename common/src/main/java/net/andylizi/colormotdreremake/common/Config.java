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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import lombok.*;
import org.jetbrains.annotations.*;

@Builder
@Getter
@Setter
public class Config {
    private final @NotNull List<String> motds;
    private final @Nullable List<String> onlineMsgs;
    private final @Nullable List<String> players;
    private @NotNull String maintenanceModeMotd;
    private @NotNull String maintenanceModeKickMsg;
    private @NotNull String tpsFormat;
    private boolean usePlaceHolderAPI;
    private boolean showPing;
    private boolean maintenanceMode;
    private boolean emergencyMode;
    private int requestLimit;
    private @NotNull String ipProvider;
    private int limitTime;
    public static Config createDefaultConfig() {
        return builder()
                .motds(Arrays.asList(
                        "&b欢迎来到我们服务器~这是第&e1&b条随机消息\n&d现在时间: &e%DATE% %TIME%",
                        "&b欢迎来到我们服务器~这是第&e2&b条随机消息\n&d在线人数: &e%ONLINE%"))
                .onlineMsgs(Arrays.asList(
                        "&2*&6查看服务器信息&2*      &a在线人数: &b%ONLINE%&d/&2%MAXPLAYER%",
                        "&2*&6查看服务器信息&2*      &a现在时间: &e%DATE% %TIME%"))
                .players(Arrays.asList(
                        "&b啦啦啦&c啦啦啦&d啦啦啦", "&b啦啦啦&c啦啦啦&d啦啦啦",
                        "&b啦啦啦&c啦啦啦&d啦啦啦", "&b啦啦啦&c啦啦啦&d啦啦啦",
                        "&b啦啦啦&c啦啦啦&d啦啦啦", "&b啦啦啦&c啦啦啦&d啦啦啦"))
                .maintenanceModeMotd("&c服务器维护中, 请等待维护完成...")
                .maintenanceModeKickMsg("&c服务器维护中,请等待维护完成再进入服务器!")
                .tpsFormat("0.0")
                .usePlaceHolderAPI(true)
                .showPing(false)
                .maintenanceMode(false)
                .emergencyMode(false)
                .requestLimit(15)
                .ipProvider("taobao")
                .limitTime(600000)
                .build();
    }

    public Config(@NotNull List<String> motds, @Nullable List<String> onlineMsgs, @Nullable List<String> players,
                  @NotNull String maintenanceModeMotd, @NotNull String maintenanceModeKickMsg, @NotNull String tpsFormat,
                  boolean usePlaceHolderAPI, boolean showPing, boolean maintenanceMode, boolean emergencyMode, int requestLimit,String ipProvider, int limitTime) {
        if (motds.isEmpty()) throw new IllegalArgumentException("motds cannot be empty");

        this.motds = new ArrayList<>(motds);
        this.onlineMsgs = onlineMsgs == null ? new ArrayList<>() : new ArrayList<>(onlineMsgs);
        this.players = players == null ? null : new ArrayList<>(players);
        this.maintenanceModeMotd = Objects.requireNonNull(maintenanceModeMotd);
        this.maintenanceModeKickMsg = Objects.requireNonNull(maintenanceModeKickMsg);
        this.tpsFormat = Objects.requireNonNull(tpsFormat);
        this.usePlaceHolderAPI = usePlaceHolderAPI;
        this.showPing = showPing;
        this.maintenanceMode = maintenanceMode;
        this.emergencyMode = emergencyMode;
        this.requestLimit = requestLimit;
        this.ipProvider = ipProvider;
        this.limitTime = 600000;
        if (requestLimit <= 0) throw new IllegalArgumentException("requestLimit");
    }

    public @NotNull String randomMotd() {
        return motds.get(ThreadLocalRandom.current().nextInt(motds.size()));
    }

    public @Nullable String randomOnlineMsg() {
        return (onlineMsgs == null || onlineMsgs.isEmpty()) ? null :
                onlineMsgs.get(ThreadLocalRandom.current().nextInt(onlineMsgs.size()));
    }

    public boolean isShowPing() {
        return showPing || onlineMsgs == null || onlineMsgs.isEmpty();
    }
}

