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
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.imageio.ImageIO;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.*;

public class ConfigManager {
    private static final List<String> DEFAULT_RESOURCES = Arrays.asList(
            "favicons/1.png", "favicons/2.png", "favicons/3.png", "favicons/maintenance.png");

    private final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private final @NotNull Path dataFolder;

    private @Nullable Config config;
    private @Nullable FaviconList favicons;

    public ConfigManager(@NotNull Path dataFolder) {
        this.dataFolder = Objects.requireNonNull(dataFolder);

        if (!Files.isDirectory(dataFolder)) try {
            Files.createDirectories(dataFolder);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public @NotNull Config getConfig() {
        if (config == null) throw new IllegalStateException("Not loaded");
        return config;
    }

    public @NotNull FaviconList getFavicons() {
        if (favicons == null) throw new IllegalStateException("Not loaded");
        return favicons;
    }

    public void loadConfig() {
        Path configFile = dataFolder.resolve("config.json");
        if (!Files.isRegularFile(configFile)) {
            this.config = Config.createDefaultConfig();
            saveConfig();
        } else try {
            this.config = gson.fromJson(Files.newBufferedReader(configFile, StandardCharsets.UTF_8), Config.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void saveConfig() {
        Path configFile = dataFolder.resolve("config.json");
        try (BufferedWriter writer = Files.newBufferedWriter(configFile,
                StandardCharsets.UTF_8, StandardOpenOption.CREATE)) {
            gson.toJson(getConfig(), Config.class, writer);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void loadFavicons() {
        favicons = null;
        Path faviconFolder = dataFolder.resolve("favicons");
        if (!Files.isDirectory(faviconFolder)) {
            favicons = new FaviconList(null, null);
            return;
        }

        final String[] supportedSuffixes = Arrays.stream(ImageIO.getReaderFileSuffixes())
                .filter(suffix -> !"tif".equals(suffix) && !"tiff".equals(suffix)).map("."::concat).toArray(String[]::new);
        List<BufferedImage> images = _walk(faviconFolder, 1)
                .filter(path -> {
                    String name = path.getFileName().toString();
                    if (FaviconList.MAINTENANCE_FAVICON_NAME.equalsIgnoreCase(name)) return false;

                    for (String suffix : supportedSuffixes) {
                        int nameLen = name.length();
                        int suffixLen = suffix.length();  // basically `name.toLowerCase().endsWith(suffix.toLowerCase())`
                        if (name.regionMatches(true, nameLen - suffixLen, suffix, 0, suffixLen))
                            return true;
                    }
                    return false;
                })
                .map(ConfigManager::_newInputStream)
                .map(BufferedInputStream::new)
                .map(ConfigManager::_readImage) // TODO: auto resize?
                .collect(Collectors.toCollection(ArrayList::new));

        BufferedImage maintenanceMode = null;
        Path maintenancePath = faviconFolder.resolve(FaviconList.MAINTENANCE_FAVICON_NAME);
        if (Files.isRegularFile(maintenancePath)) {
            maintenanceMode = _readImage(new BufferedInputStream(_newInputStream(maintenancePath)));
        }

        favicons = new FaviconList(images, maintenanceMode);
    }

    public void saveDefaultResources() {
        for (String name : DEFAULT_RESOURCES) {
            saveResource(name, false);
        }
    }

    public void saveResource(@NotNull String name, boolean replaceExisting) {
        Path target = dataFolder.resolve(name);
        if (!replaceExisting && Files.isRegularFile(target)) return;

        InputStream in = getClass().getResourceAsStream("/".concat(name));
        if (in == null) throw new IllegalArgumentException("Resource '" + name + "' not found!");
        try (BufferedInputStream bin = new BufferedInputStream(in)) {
            Files.createDirectories(target.getParent());
            Files.copy(bin, target, replaceExisting ?
                    new CopyOption[0] :
                    new CopyOption[]{ StandardCopyOption.REPLACE_EXISTING });
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static InputStream _newInputStream(Path path) {
        try {
            return Files.newInputStream(path);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static BufferedImage _readImage(InputStream in) {
        try {
            return ImageIO.read(in);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static Stream<Path> _walk(Path start, int maxDepth) {
        try {
            return Files.walk(start, maxDepth);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
