package gitee.xzcxrj.mypotion.potion;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class Potion {

    private final String display;
    private final int cooldown;
    private final AttributeEffect attribute;

    public Potion(ConfigurationSection section) {
        String string = section.getString("display", "§c(None Display)");
        this.display = string.replaceAll("&", "§");
        this.cooldown = section.getInt("cooldown", 1);
        this.attribute = new AttributeEffect(this, section.getConfigurationSection("attribute"));
    }

    public void usePotion(Player player) {
        attribute.run(player);
    }

    public AttributeEffect getAttribute() {
        return attribute;
    }

    public String getDisplay() {
        return display;
    }

    public int getCooldown() {
        return cooldown;
    }
}
