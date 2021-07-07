package coding.json.practice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {
        OriginBasic ob = new OriginBasic();
        ob.basic();
        ob.basicGetList();
        ob.basicPutList();


        Origin2 o2 = new Origin2();
        o2.basicGet();
        o2.basicPost();
    }

}
