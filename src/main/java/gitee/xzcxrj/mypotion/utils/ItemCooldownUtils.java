package gitee.xzcxrj.mypotion.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ItemCooldownUtils {

    private static boolean isUse = false;
    private static String version;
    private static Class<?> craftHumanEntityClass;
    private static Method getHandle;
    private static Method getItemInMainHand;
    private static Method getItem;
    private static Method getCooldownTracker;
    private static Method setItemCooldown;

    static {
        version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        String methodName;
        switch (version) {
            case "v1_9_R1":
                methodName = "da";
                break;
            case "v1_9_R2":
                methodName = "db";
                break;
            case "v1_10_R1":
                methodName = "df";
                break;
            case "v1_11_R1":
                methodName = "di";
                break;
            case "v1_12_R1":
                methodName = "getCooldownTracker";
                break;
            default:
                methodName = null;
                break;
        }
        if (methodName != null) {
            try {
                craftHumanEntityClass = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftHumanEntity");
                getHandle = craftHumanEntityClass.getMethod("getHandle");
                Class<?> entityHumanClass = Class.forName("net.minecraft.server." + version + ".EntityHuman");
                getItemInMainHand = entityHumanClass.getMethod("getItemInMainHand");
                getItem = Class.forName("net.minecraft.server."+ version + ".ItemStack").getMethod("getItem");
                getCooldownTracker = entityHumanClass.getMethod(methodName);
                setItemCooldown = Class.forName("net.minecraft.server." + version + ".ItemCooldown").getMethod("a", Class.forName("net.minecraft.server." + version + ".Item"), int.class);
                isUse = true;
            } catch (ClassNotFoundException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setItemCooldownInHand(Player player, int tick) {
        if (!isUse) {
            return;
        }
        Object cast = craftHumanEntityClass.cast(player);
        try {
            Object entityHuman = getHandle.invoke(cast);
            Object itemCooldown = getCooldownTracker.invoke(entityHuman);
            Object itemStack = getItemInMainHand.invoke(entityHuman);
            Object item = getItem.invoke(itemStack);
            setItemCooldown.invoke(itemCooldown, item, tick);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
