package gitee.xzcxrj.mypotion.potion;

import gitee.xzcxrj.mypotion.MyPotion;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class PotionManager {

    private Map<String, Potion> potions = new HashMap<>();
    private final File folder = new File(MyPotion.getPlugin().getDataFolder(), "potions");

    public PotionManager() {
    }

    public void init() {
        this.potions = new HashMap<>();
        if (!folder.exists()) {
            MyPotion.getPlugin().saveResource("potions\\example.yml", true);
        }
        File[] files = this.folder.listFiles((dir, name) -> name.endsWith(".yml"));
        if (files == null) {
            return;
        }
        for (File file : files) {
            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
            for (String key : yaml.getConfigurationSection("").getKeys(false)) {
                Potion potion = new Potion(yaml.getConfigurationSection(key));
                this.potions.put(potion.getDisplay().replaceAll("&", "§"), potion);
            }
        }
    }

    public Potion getPotion(String display) {
        return potions.get(display);
    }

    public Potion getPotion(ItemStack itemStack) {
        return this.getPotion(itemStack.getItemMeta().getDisplayName());
    }

    public Collection<Potion> getPotions() {
        return potions.values();
    }
}
