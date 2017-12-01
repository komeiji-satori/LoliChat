package moe.satori;

/**
 *
 * @author satori
 */
import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import com.github.kevinsawicki.http.HttpRequest;

public class Main extends JavaPlugin {

    public static String address;
    public static String data;
    public static Plugin my;
    public static int type;
    public static int sleep;
    public static boolean debug;

    public static Map<Integer, HashMap<String, String>> qwq = new HashMap<>();
    public static Lock lock = new ReentrantLock();

//    public static void main(String[] args) throws ParseException {
//        System.out.println(Push.push("Satori", "w"));
//    }

    public static String cURL(String URL, String Type, String Params) {
        String response;
        if (Type != null) {
            response = HttpRequest.post(URL).send(Params).body();
        } else {
            response = HttpRequest.get(URL).body();
        }
        return response;
    }

    @Override
    public void onEnable() {
        my = this;
        saveDefaultConfig();
        this.setConfig();
        this.getServer().getPluginManager().registerEvents(new AsyncChat(), this);
        loadLib();
    }

    private void setConfig() {//加载参数
        reloadConfig();//读取配置文件
        address = this.getConfig().getString("InterfaceAddress");//↓
        type = this.getConfig().getInt("Type");//↓
        debug = this.getConfig().getBoolean("debug");//↓
        sleep = this.getConfig().getInt("sleep");//参数赋值
        if (type == 2) {
            this.getLogger().info("添加CheckPush线程");
            new Thread(new CheckThread()).start();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {//重读指令
        if (sender.isOp()) {
            setConfig();
            sender.sendMessage("Done.");
            return true;
        }
        sender.sendMessage("只有OP才能执行");
        return true;
    }

    public void loadLib() {
        File libPath = getServer().getPluginManager().getPlugin("LoliChat").getDataFolder();
        File[] jarFiles = libPath.listFiles((dir, name) -> name.endsWith(".jar") || name.endsWith(".class"));

        if (jarFiles != null) {
            Method method = null;
            try {
                method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            boolean accessible = method.isAccessible();
            try {
                if (accessible == false) {
                    method.setAccessible(true);
                }
                URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
                for (File file : jarFiles) {
                    URL url = file.toURI().toURL();
                    try {
                        method.invoke(classLoader, url);
                        System.out.println("读取前置" + file.getName() + "成功!");
                    } catch (Exception e) {
                        System.out.println("读取库文件失败!插件将无法运行!");
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } finally {
                method.setAccessible(accessible);
            }
        }
    }
}
