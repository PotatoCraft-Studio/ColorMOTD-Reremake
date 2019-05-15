import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class BungeeCommandListener extends Command {
    BungeeMain plugin;

    public BungeeCommandListener(BungeeMain plugin) {
        super("colormotd");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("colormotd.admin")) {
            sender.sendMessage(new TextComponent(plugin.prefix + ChatColor.RED + "权限不足"));
            return;
        }
        if (args.length == 0) {
            sender.sendMessage(new TextComponent(plugin.prefix + "可用指令: smode underattack lsblockedip reload version"));
        }
        switch (args[0]) {
            case "smode":
                if (plugin.getConfig().isInMaintenance()) {
                    plugin.getConfig().setInMaintenance(false);
                    plugin.getConfigManager().saveConfig();
                    sender.sendMessage(new TextComponent(plugin.prefix + "维护模式已关闭"));
                } else {
                    plugin.getConfig().setInMaintenance(true);
                    plugin.getConfigManager().saveConfig();
                    for (ProxiedPlayer player : plugin.getProxy().getPlayers()) {
                        if (!player.hasPermission("colormotd.smode.join"))
                            player.disconnect(new TextComponent(plugin.getFormatter().applyPlaceHolder(plugin.getConfig().getMaintenanceModeKickMsg(), player.getAddress().getHostString())));
                    }
                    sender.sendMessage(new TextComponent(plugin.prefix + "维护模式已开启，除服务器管理员外其他玩家将无法加入服务器，如需关闭请再输入一次指令"));
                }
                break;
            case "reload":
                plugin.getConfigManager().reloadConfig();
                sender.sendMessage(new TextComponent(plugin.prefix + "使用Bungee兼容模式对配置文件进行了热重读，部分功能可能不会重载，对BungeeCord进行重启以完整重读"));
                break;
            case "underattack":
                if (plugin.getConfig().isUnderAttack()) {
                    plugin.getConfig().setUnderAttack(false);
                    plugin.getConfigManager().saveConfig();
                    sender.sendMessage(new TextComponent(plugin.prefix + "防御模式已关闭，MOTD已重新开放"));
                } else {
                    plugin.getConfig().setUnderAttack(true);
                    plugin.getConfigManager().saveConfig();
                    sender.sendMessage(new TextComponent(plugin.prefix + "防御模式已开启，任何人将无法看到MOTD"));
                }
                break;
            case "version":
                sender.sendMessage(new TextComponent(plugin.prefix + " version " + plugin.getDescription().getVersion()));
                break;
            case "listblockedip":
                for (String ip : plugin.getBungeeMotdListener().getFirewall().getRequestMap().keySet()) {
                    if (plugin.getBungeeMotdListener().getFirewall().isBlocked(ip))
                        sender.sendMessage(new TextComponent(plugin.prefix + "IP: " + " Count: " + plugin.getBungeeMotdListener().getFirewall().getRequestMap().get(ip)));
                }
                sender.sendMessage(new TextComponent(plugin.prefix + "所有被屏蔽IP已获取完毕."));
                break;
        }
    }
}
