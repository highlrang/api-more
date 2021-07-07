package coding.json.practice;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;

public class Origin {
    public static void main(String[] args) {
        try {
            URL url = new URL("http://127.0.0.1");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responseCode = conn.getResponseCode();
            Scanner sc = new Scanner(url.openStream());
            String line = sc.nextLine(); // for문으로 +=
            // JsonParser parse = new JsonParser();
            // JSONObject obj = new (JsonObject)parse.parse(line);
            // obj.get("key");

        }catch(MalformedURLException e){
            e.printStackTrace();

        } catch (ProtocolException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
