package gitee.xzcxrj.mypotion;

import gitee.xzcxrj.attributehook.AttributeHook;
import gitee.xzcxrj.attributehook.api.event.ReloadEvent;
import gitee.xzcxrj.mypotion.api.PlayerData;
import gitee.xzcxrj.mypotion.listener.PlayerListener;
import gitee.xzcxrj.mypotion.logger.PotionLogger;
import gitee.xzcxrj.mypotion.potion.PotionManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class MyPotion extends JavaPlugin  implements Listener {

    private static MyPotion plugin;

    @Override
    public void onLoad() {
        plugin = this;
    }

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.reloadConfig();
        api.init();
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    private void on(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        api.PLAYER_DATA.remove(player);
        AttributeHook.api.removeEntityAPIData(this, player.getUniqueId());
    }

    @EventHandler
    private void on(ReloadEvent event) {
        AttributeHook.api.removePluginAllEntityData(this);
        this.saveDefaultConfig();
        this.reloadConfig();
        api.init();
        PotionLogger.log(event.getSender(), "%t §c插件已重载");
    }

    public static final class api {
        private static final Map<Player, PlayerData> PLAYER_DATA = new HashMap<>();
        private static final PotionManager potionManager = new PotionManager();

        private static void init() {
            potionManager.init();
            PotionLogger.log("%t §aLoaded §f{0} §aPotions", potionManager.getPotions().size());
        }

        public static PlayerData getPlayerData(Player player) {
            if (!PLAYER_DATA.containsKey(player)) {
                PLAYER_DATA.put(player, new PlayerData(player));
            }
            return PLAYER_DATA.get(player);
        }

        public static PotionManager getPotionManager() {
            return potionManager;
        }
    }

    public static MyPotion getPlugin() {
        return plugin;
    }
}
