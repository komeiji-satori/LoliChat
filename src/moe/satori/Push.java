package moe.satori;

import org.json.simple.parser.*;
import org.json.simple.JSONObject;
import static moe.satori.Main.cURL;
/**
 *
 * @author satori
 */
public class Push {
    public static boolean push(String username, String message) throws ParseException {
        String json = cURL(Main.address, "POST", "body=" + buildJson(username, message));
        //String json = cURL("http://127.0.0.1:2333/api.php", "POST", "body=" + buildJson(username, message));
        Object obj = new JSONParser().parse(json);
        JSONObject result = (JSONObject) obj;
        if (result.get("status").equals("true")) {
            return true;
        } else {
            return false;
        }
    }

    public static String buildJson(String username, String message) {
        JSONObject json = new JSONObject();
        json.put("username", username);
        json.put("message", message);
        return json.toString();
    }
}
