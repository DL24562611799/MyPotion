package gitee.xzcxrj.mypotion.api;

import gitee.xzcxrj.mypotion.MyPotion;
import gitee.xzcxrj.mypotion.logger.PotionLogger;
import gitee.xzcxrj.mypotion.potion.Potion;
import gitee.xzcxrj.mypotion.utils.ItemCooldownUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerPotion {

    private final Player player;
    private final Potion potion;
    private long cooldown;

    public PlayerPotion(Player player, Potion potion) {
        this.player = player;
        this.potion = potion;
    }

    public void run(ItemStack item) {
        FileConfiguration config = MyPotion.getPlugin().getConfig();
        if (this.isCooldown()) {
            send(player, config.getString("message.cooldown"), potion.getDisplay(), this.getCooldown());
            return;
        }
        this.cooldown = System.currentTimeMillis() + (potion.getCooldown() * 1000L);
        potion.usePotion(player);
        send(player, config.getString("message.use"), potion.getDisplay(), potion.getCooldown(), potion.getAttribute().getContinued());
        if (config.getBoolean("enable-item-cooldown")) {
            ItemCooldownUtils.setItemCooldownInHand(player, potion.getCooldown() * 20);
        }
        if (item.getAmount() > 1) {
            item.setAmount(item.getAmount() - 1);
        }else {
            item.setAmount(0);
        }
    }

    public boolean isCooldown() {
        return cooldown > System.currentTimeMillis();
    }

    public long getCooldown() {
        long now = System.currentTimeMillis();
        return (int) ((cooldown - now) / 1000);
    }

    public static void send(Player player, String str, Object... objects) {
        if (str != null && !str.toUpperCase().contains("NONE")) {
            for (int i = 0; i < objects.length; i++) {
                str = str.replaceAll("\\{"+i+"}", objects[i].toString());
            }
            player.sendMessage(str.replaceAll("&", "§"));
        }
    }
}
