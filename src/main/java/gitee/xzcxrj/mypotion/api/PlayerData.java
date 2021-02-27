package gitee.xzcxrj.mypotion.api;

import gitee.xzcxrj.mypotion.MyPotion;
import gitee.xzcxrj.mypotion.potion.Potion;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PlayerData {
    private final Player player;
    private final Map<Potion, PlayerPotion> potions = new HashMap<>();

    public PlayerData(Player player) {
        this.player = player;
        for (Potion potion : MyPotion.api.getPotionManager().getPotions()) {
            potions.put(potion, new PlayerPotion(player, potion));
        }
    }

    public Player getPlayer() {
        return player;
    }

    public Optional<PlayerPotion> getPlayerPotion(Potion potion) {
        return Optional.ofNullable(potions.get(potion));
    }

    public Collection<PlayerPotion> getPlayerPotions() {
        return potions.values();
    }
}
