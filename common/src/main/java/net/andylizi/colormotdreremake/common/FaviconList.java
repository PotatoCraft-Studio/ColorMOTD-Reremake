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

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import lombok.*;
import org.jetbrains.annotations.*;

@Getter
@Setter
public class FaviconList {
    public static final String MAINTENANCE_FAVICON_NAME = "maintenance.png";

    private final @NotNull List<BufferedImage> list;
    private @Nullable BufferedImage maintenanceMode;

    public FaviconList(@Nullable List<BufferedImage> list, @Nullable BufferedImage maintenanceMode) {
        this.list = list == null ? new ArrayList<>() : new ArrayList<>(list);
        this.maintenanceMode = maintenanceMode;
    }

    public @Nullable BufferedImage randomFavicon() {
        return list.isEmpty() ? null : list.get(ThreadLocalRandom.current().nextInt(list.size()));
    }

    public @Nullable BufferedImage chooseFavicon(boolean inMaintenance) {
        BufferedImage favicon = null;
        if (inMaintenance) favicon = maintenanceMode;
        return favicon == null ? randomFavicon() : favicon;
    }
}
