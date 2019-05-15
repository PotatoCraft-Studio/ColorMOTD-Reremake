import net.andylizi.core.InternalPlaceHolder;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeTextFormatter extends InternalPlaceHolder {
    Plugin bungee;
        public BungeeTextFormatter(Plugin bungee){
            this.bungee = bungee;
        }
        public String applyPlaceHolder(String text,String ip){
            text = super.applyPlaceHolder(text,ip);
            text = text.replaceAll("%online%",String.valueOf(bungee.getProxy().getOnlineCount()));
            text = text.replaceAll("%maxonline%", String.valueOf(bungee.getProxy().getPluginConfig().getPlayerLimit()));
            text = text.replaceAll("%servers%",String.valueOf(bungee.getProxy().getServers().size()));
            return text;
        }
}
