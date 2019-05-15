package net.andylizi.bukkit;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import lombok.Getter;
import net.andylizi.core.Config;
import net.andylizi.core.ConfigManager;
import net.andylizi.core.Firewall;
import net.andylizi.core.Icon;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitMain extends JavaPlugin {
    @Getter public ConfigManager configManager;
    @Getter public Config pluginConfig;
    @Getter public Icon icon;
    @Getter public ProtocolManager protocolManager;
    @Getter public BukkitTextFormatter formatter;
    final public String prefix = ChatColor.AQUA+"["+ChatColor.GOLD+"ColorMOTD"+ChatColor.AQUA+"] "+ChatColor.GREEN;
    @Getter public BukkitMotdPacketListener bukkitMotdPacketListener;
    @Override
    public void onEnable() {
        getLogger().info("ColorMOTD Reremake 已加载");
        getLogger().info("初始化配置文件管理器...");
        configManager = new ConfigManager(getDataFolder());
        getLogger().info("加载配置文件...");
        pluginConfig = configManager.getConfig();
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
        getLogger().info("初始化文本格式化工具...");
        formatter = new BukkitTextFormatter(this);

        getLogger().info("注册 ProtocolLib 事件监听器...");
        bukkitMotdPacketListener = new BukkitMotdPacketListener(this);
        protocolManager.addPacketListener(bukkitMotdPacketListener);

        getLogger().info("注册 Bukkit 事件监听器...");
        Bukkit.getPluginManager().registerEvents(new BukkitEventListener(this),this);

        getLogger().info("工作全部完成 OK <3");
    }

    @Override
    public void onDisable() {
        getLogger().info("正在注销 ProtocolLib 监听器");
        protocolManager.removePacketListeners(this);
        getLogger().info("ColoMOTD Reremake 已卸载");
        super.onDisable();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("smode")){
            Bukkit.dispatchCommand(sender, "colormotd smode");
            return true;
        }
        if(command.getName().equalsIgnoreCase("cmoder")){
            Bukkit.dispatchCommand(sender, "colormotd reload");
            return true;
        }
        if(command.getName().equalsIgnoreCase("underattack")||command.getName().equalsIgnoreCase("dmode")){
            Bukkit.dispatchCommand(sender, "colormotd underattack");
            return true;
        }
        if(!command.getName().equalsIgnoreCase("colormotd"))
            return false;
        if(!sender.hasPermission("colormotd.admin")) {
            sender.sendMessage(prefix+ChatColor.RED+"权限不足");
            return true;
        }
        if(args.length == 0){
            sender.sendMessage(prefix+"可用指令: smode underattack lsblockedip reload version");
        }
        switch (args[0]) {
            case "smode":
                if(getPluginConfig().isInMaintenance()){
                    getPluginConfig().setInMaintenance(false);
                    getConfigManager().saveConfig();
                    sender.sendMessage(prefix+"维护模式已关闭");
                }else{
                    getPluginConfig().setInMaintenance(true);
                    getConfigManager().saveConfig();
                    for (Player player: ReflectFactory.getPlayers()){
                        if(!(player.isOp() || player.hasPermission("colormotd.smode.join")))
                            player.kickPlayer(getFormatter().applyPlaceHolder(getPluginConfig().getMaintenanceModeKickMsg(),player.getAddress().getHostString() ));
                    }
                    sender.sendMessage(prefix+"维护模式已开启，除服务器管理员外其他玩家将无法加入服务器，如需关闭请再输入一次指令");
                }
                break;
            case "reload":
                this.getPluginLoader().disablePlugin(this);
                this.getPluginLoader().enablePlugin(this);
                sender.sendMessage(prefix+"插件重载已完成");
                break;
            case "underattack":
                if(getPluginConfig().isUnderAttack()){
                    getPluginConfig().setUnderAttack(false);
                    getConfigManager().saveConfig();
                    sender.sendMessage(prefix+"防御模式已关闭，MOTD已重新开放");
                }else{
                    getPluginConfig().setUnderAttack(true);
                    getConfigManager().saveConfig();
                    sender.sendMessage(prefix+"防御模式已开启，任何人将无法看到MOTD");
                }
                break;
            case "version":
                sender.sendMessage(prefix+" version "+this.getDescription().getVersion());
                break;
            case "listblockedip":
                for (String ip : bukkitMotdPacketListener.getFirewall().getRequestMap().keySet()){
                    if(bukkitMotdPacketListener.getFirewall().isBlocked(ip))
                        sender.sendMessage(prefix+"IP: "+" Count: "+bukkitMotdPacketListener.getFirewall().getRequestMap().get(ip));
                }
                sender.sendMessage(prefix+"所有被屏蔽IP已获取完毕.");
        }
        return true;

    }
}
