package net.andylizi.bukkit;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import net.andylizi.core.Config;
import net.andylizi.core.ConfigManager;
import net.andylizi.core.Icon;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitMain extends JavaPlugin {
    ConfigManager configManager;
    Config config;
    Icon icon;
    ProtocolManager protocolManager;
    @Override
    public void onEnable() {
        getLogger().info("ColorMOTD Reremake 已加载");
        getLogger().info("初始化配置文件管理器...");
        configManager = new ConfigManager(getDataFolder());
        getLogger().info("加载配置文件...");
        config = configManager.getConfig();
        getLogger().info("加载 MOTD 图标...");
        icon = configManager.getIcon();
        getLogger().info("加载 ProtocolLib ...");

        Plugin protocolLib = Bukkit.getPluginManager().getPlugin("ProtocolLib");
        if(protocolLib==null){
            getLogger().severe("无法加载 ProtocolLib 库，ColorMOTD 无法正常工作，正在退出...");
            this.getPluginLoader().disablePlugin(this);
            return;
        }
        getLogger().info("以 ProtocolLib "+protocolLib.getDescription().getVersion()+" 作为依赖加载中...");
        try{
            protocolManager = ProtocolLibrary.getProtocolManager();
        }catch (Throwable throwable){
            getLogger().severe("无法加载 ProtocolLib 库，ColorMOTD 无法正常工作，正在退出...");
            getLogger().severe("请报告此错误给作者："+throwable.getMessage());
            this.getPluginLoader().disablePlugin(this);
            return;
        }

    }
}
