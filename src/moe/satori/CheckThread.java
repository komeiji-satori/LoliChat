package moe.satori;

/**
 *
 * @author satori
 */

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.parser.ParseException;

/**
 * Created by ALiKOMI on 2017/6/16.
 */
public class CheckThread extends Thread {//队列推送线程

    private static Push push = new Push();

    @Override
    public void run() {
        while (Main.type == 2) {
            Main.lock.lock();
            Map<Integer, HashMap<String, String>> map = Main.qwq;
            Main.qwq = new HashMap<>();
            Main.lock.unlock();

            if (map == null) {
                continue;
            }
            if (map.isEmpty()) {
                continue;
            }

            map.forEach((id, data) -> {
                if (Main.debug) {
                    Main.my.getLogger().info("队列推送开始");
                    Main.my.getLogger().info("当前队列：" + id);
                }
                data.forEach((user, message) -> {

                    try {
                        push.push(user, message);
                    } catch (ParseException ex) {
                        Logger.getLogger(CheckThread.class.getName()).log(Level.SEVERE, null, ex);
                    }

                });
            });

            try {
                Thread.sleep(Main.sleep);
            } catch (InterruptedException e) {
                Main.my.getLogger().info("sleep异常");
            }
        }
    }
}
