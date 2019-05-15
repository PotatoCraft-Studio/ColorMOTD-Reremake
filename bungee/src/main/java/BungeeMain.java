import lombok.Getter;
import net.andylizi.core.Config;
import net.andylizi.core.ConfigManager;
import net.andylizi.core.Icon;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeMain extends Plugin {
    @Getter
    public ConfigManager configManager;
    @Getter
    public Config config;
    @Getter
    public Icon icon;
    @Getter
    public BungeeTextFormatter formatter;
    final public String prefix = ChatColor.AQUA + "[" + ChatColor.GOLD + "ColorMOTD-BC" + ChatColor.AQUA + "] " + ChatColor.GREEN;
    @Getter
    BungeeMotdListener bungeeMotdListener;

    @Override
    public void onEnable() {
        getLogger().info("ColorMOTD Reremake 已加载");
        getLogger().info("初始化配置文件管理器...");
        configManager = new ConfigManager(getDataFolder());
        getLogger().info("加载配置文件...");
        config = configManager.getConfig();
        getLogger().info("加载 MOTD 图标...");
        icon = configManager.getIcon();
        getLogger().info("初始化文本格式化工具...");
        formatter = new BungeeTextFormatter(this);

        getLogger().info("注册 Bungee 事件监听器...");
        getProxy().getPluginManager().registerListener(this, new BungeeEventListener(this));
        bungeeMotdListener = new BungeeMotdListener(this);
        getProxy().getPluginManager().registerListener(this, bungeeMotdListener);
        getProxy().getPluginManager().registerCommand(this, new BungeeCommandListener(this));

        getLogger().info("工作全部完成 OK <3");

    }

    @Override
    public void onDisable() {
        super.onDisable();
    }


}
