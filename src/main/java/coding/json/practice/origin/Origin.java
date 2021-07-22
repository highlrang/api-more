package coding.json.practice.origin;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;

public class Origin {
    public void originConnect() {
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

        } catch (IOException e) {
            e.printStackTrace();

        }
    }


}
