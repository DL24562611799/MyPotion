package gitee.xzcxrj.mypotion.listener;

import gitee.xzcxrj.mypotion.MyPotion;
import gitee.xzcxrj.mypotion.api.PlayerData;
import gitee.xzcxrj.mypotion.api.PlayerPotion;
import gitee.xzcxrj.mypotion.logger.PotionLogger;
import gitee.xzcxrj.mypotion.potion.Potion;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PlayerListener implements Listener {

    public static final ItemStack AIR = new ItemStack(Material.AIR);

    private static final Map<Player, Long> map = new HashMap<>();

    private static boolean noInteract(Player player) {
        Long cooldown = map.getOrDefault(player, 0L);
        return cooldown > System.currentTimeMillis() ;
    }

    private static boolean hasBlock(Block block) {
        if (block != null && block.getType() != Material.AIR) {
            List<String> list = MyPotion.getPlugin().getConfig().getStringList("interact-block");
            if (list != null) {
                return list.contains(block.getType().name());
            }
        }
        return false;
    }

    @EventHandler
    void on(PlayerQuitEvent event) {
        map.remove(event.getPlayer());
    }

    @EventHandler
    void on(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (noInteract(player)) {
            return;
        }
        map.put(player, System.currentTimeMillis()+1000);
        if (!event.hasItem()) {
            return;
        }
        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            return;
        }
        if (hasBlock(event.getClickedBlock())) {
            return;
        }
        run(player, event.getItem());
    }

    @EventHandler
    void on(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if (noInteract(player)) {
            return;
        }
        map.put(player, System.currentTimeMillis()+1000);
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        ItemStack offHand = player.getInventory().getItemInOffHand();
        if (isItem(mainHand)) {
            run(player, mainHand);
        }else if (isItem(offHand)) {
            run(player, offHand);
        }
    }

    private void run(Player player, ItemStack item) {
        if (item != null && MyPotion.getPlugin().getConfig().getBoolean("enable-debug")) {
            PotionLogger.log(player, "%t §6当前物品的类型是: §a{0}", item.getType().name());
        }
        if (!isItem(item)) {
            return;
        }
        Potion potion = MyPotion.api.getPotionManager().getPotion(item);
        if (potion == null) {
            return;
        }
        PlayerData playerData = MyPotion.api.getPlayerData(player);
        Optional<PlayerPotion> optional = playerData.getPlayerPotion(potion);
        if (!optional.isPresent()) {
            return;
        }
        optional.get().run(item);
    }

    public static boolean isItem(ItemStack itemStack) {
        return itemStack != null && !itemStack.isSimilar(AIR) && itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName();
    }
}
