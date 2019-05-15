package net.andylizi.bukkit;

import me.clip.placeholderapi.PlaceholderAPI;
import net.andylizi.core.InternalPlaceHolder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class BukkitTextFormatter extends InternalPlaceHolder {
    private BukkitMain plugin;
    public BukkitTextFormatter(BukkitMain plugin){
        this.plugin = plugin;
    }
    public String applyPlaceHolder(String text,String ip){
        text = super.applyPlaceHolder(text,ip);
        text = text.replaceAll("%tps%",String.valueOf(ReflectFactory.getPlayers().length));
        text = text.replaceAll("%online%",String.valueOf(Bukkit.getOnlinePlayers().size()));
        text = text.replaceAll("%maxonline%", String.valueOf(Bukkit.getMaxPlayers()));
        text = text.replaceAll("%playedbefore%",String.valueOf(Bukkit.getOfflinePlayers().length));
        if(plugin.usePlaceHolderAPI)
            text = PlaceholderAPI.setBracketPlaceholders(Bukkit.getOfflinePlayer(UUID.fromString("ColorMOTD")),text);
        return text;
    }
}
