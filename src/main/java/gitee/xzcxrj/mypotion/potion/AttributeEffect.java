package gitee.xzcxrj.mypotion.potion;

import gitee.xzcxrj.attributehook.AttributeData;
import gitee.xzcxrj.attributehook.AttributeHook;
import gitee.xzcxrj.mypotion.MyPotion;
import gitee.xzcxrj.mypotion.api.PlayerPotion;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AttributeEffect {
    private final Potion potion;
    private final int continued;
    private final AttributeData data;

    public AttributeEffect(Potion potion, ConfigurationSection section) {
        this.potion = potion;
        this.continued = section.getInt("continue", 1);
        List<String> list = section.getStringList("list");
        if (list != null) {
            data = AttributeHook.api.loadListData(list);
        }else {
            data = new AttributeData();
        }
    }

    public void run(Player player) {
        CompletableFuture.runAsync(() -> {
            AttributeHook.api.setEntityAPIData(MyPotion.getPlugin(), player.getUniqueId(), data);
            AttributeHook.api.attributeUpdate(player);
            new BukkitRunnable() {
                @Override
                public void run() {
                    AttributeHook.api.setEntityAPIData(MyPotion.getPlugin(), player.getUniqueId(), new AttributeData());
                    AttributeHook.api.attributeUpdate(player);
                    PlayerPotion.send(player, MyPotion.getPlugin().getConfig().getString("message.effect-end"), potion.getDisplay());
                }
            }.runTaskLater(MyPotion.getPlugin(), (continued * 20L));
        });
    }

    public int getContinued() {
        return continued;
    }
}
