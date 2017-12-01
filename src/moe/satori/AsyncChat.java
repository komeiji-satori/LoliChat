package moe.satori;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.parser.ParseException;

public class AsyncChat implements Listener {

    private static Push push;

    public AsyncChat() {
        push = new Push();
    }

    @EventHandler
    public void onAPC(AsyncPlayerChatEvent event) {
        if (push == null) {
            Main.my.getLogger().info("Push对象异常！");
            return;
        }

        final String pname = event.getPlayer().getName();
        String message = event.getMessage();
//        try {
//            message = Base64.getEncoder().encodeToString(event.getMessage().getBytes("utf-8"));
//        } catch (UnsupportedEncodingException e) {
//            if (Main.debug) {
//                e.printStackTrace();
//            }
//            Main.my.getLogger().info("Base64编码错误");
//            return;
//        }
        if (Main.type == 1) {
            final String finalMessage = message;
            new BukkitRunnable() {
                public void run() {
                    if (Main.debug) {
                        Main.my.getLogger().info("实时推送开始");
                    }
                    try {
                        push.push(pname, finalMessage);
                    } catch (ParseException ex) {
                        Logger.getLogger(AsyncChat.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }.runTaskAsynchronously(Main.my);
        } else if (Main.type == 2) {
            Main.lock.lock();
            String finalMessage = message;
            Main.qwq.put(Main.qwq.size(), new HashMap<String, String>() {
                {
                    put(pname, finalMessage);
                }
            });
            Main.lock.unlock();
        } else {
            Main.my.getLogger().info("配置文件错误");
        }
    }
}
