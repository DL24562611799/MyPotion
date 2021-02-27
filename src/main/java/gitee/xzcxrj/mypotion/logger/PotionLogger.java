package gitee.xzcxrj.mypotion.logger;

import gitee.xzcxrj.mypotion.MyPotion;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class PotionLogger {

    public static void log(String message, Object... objects) {
        PotionLogger.log(Bukkit.getConsoleSender(), message, objects);
    }

    public static void log(CommandSender sender, String message, Object... objects) {
        String append = append(message, objects).replace("%t", "["+ MyPotion.getPlugin().getName() +"]");
        sender.sendMessage(append);
    }

    public static String append(String str, Object... objects) {
        if (objects.length > 0) {
            for (int i = 0; i < objects.length; i++) {
                str = str.replaceAll("\\{"+i+"}", objects[i].toString());
            }
        }
        return str;
    }
}
