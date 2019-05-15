package net.andylizi.bukkit;

import net.andylizi.core.InternalPlaceHolder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitTextFormatter extends InternalPlaceHolder {
    private JavaPlugin plugin;
    public BukkitTextFormatter(JavaPlugin plugin){
        this.plugin = plugin;
    }
    public String applyPlaceHolder(String text,String ip){
        text = super.applyPlaceHolder(text,ip);
        text = text.replaceAll("%tps%",String.valueOf(ReflectFactory.getPlayers().length));
        text = text.replaceAll("%online%",String.valueOf(Bukkit.getOnlinePlayers().size()));
        text = text.replaceAll("%playedbefore%",String.valueOf(Bukkit.getOfflinePlayers().length));
        return text;
    }
}
