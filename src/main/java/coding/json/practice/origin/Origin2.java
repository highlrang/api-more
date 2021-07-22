package coding.json.practice.origin;

import coding.json.practice.vo.UserVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class Origin2 {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static HttpURLConnection conn = null;

    // controller 연동하기
    public void basicGet() throws IOException {
        // readValue
        try {
            URL url = new URL("http://127.0.0.1:8080/test");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoInput(true);
            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) log.info("");

            // InputStreamReader > StringBuilder
            StringBuilder stringBuilder;
            try (BufferedReader inputStream = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()))) { // utf-8

                String line;
                stringBuilder = new StringBuilder();

                // inputStream.lines().forEach(stringBuilder::append);

                while ((line = inputStream.readLine()) != null) {
                    log.info(this.getClass() + " GET method의 line : " + line.trim());
                    stringBuilder.append(line.trim());
                }

                log.info(this.getClass() + " GET method의 stringBuilder : " + stringBuilder);

                // 최종
                List<UserVO> userList = Arrays.asList(mapper.readValue(stringBuilder.toString(), UserVO[].class));
                userList.forEach(u->log.info(this.getClass() + " GET Method로 받은 값들 이름 필드 : " + u.getName()));
            }

        }finally{
            conn.disconnect();
        }

    }

    public void basicPost() throws IOException {
        // writeValue

        try {
            URL url = new URL("http://127.0.0.1:8080/test");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            // conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            // conn.setRequestProperty("User-Agent", "Java client");
            conn.setDoOutput(true);
            // conn.connect();

            // if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) log.info(String.valueOf(conn.getErrorStream()));

            UserVO user1 = new UserVO(1, "Post controller testing ...");
            UserVO user2 = new UserVO(1, "Second Post Controller testing ...");
            List<UserVO> userList = new ArrayList<>();
            userList.add(user1);
            userList.add(user2);

            // 객체를 json으로 변환해서 post로 보내기
            String json = mapper.writeValueAsString(userList);
            String json2 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(userList);
            // byte[] jsonByte = mapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(userList);
            // byte[] input = json.getBytes(StandardCharsets.UTF_8);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            bw.write(json2);
            bw.flush();
            bw.close();

            log.info(String.valueOf(conn.getResponseCode()));
            log.info(json2);

            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line.trim());
                }
                log.info(this.getClass() + " POST method의 stringBuilder : " + sb.toString());
            }


        } finally {
            conn.disconnect();
        }
    }


}

