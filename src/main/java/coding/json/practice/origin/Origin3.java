package coding.json.practice.origin;

import coding.json.practice.vo.UserVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
public class Origin3 {

    private static final RestTemplate restTemplate = new RestTemplate();

    public void basicGet() throws IOException {

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(5000);
        factory.setConnectTimeout(5000);
        /*
        HttpClient httpClient = HttpClientBuilder.create()
                .setMaxConnTotal(200) // 연결 유지 수
                .setMaxConnPerRoute(20)
                .build();
        factory.setHttpClient(httpClient);
        */

        HttpHeaders header = new HttpHeaders();
        header.setContentType(APPLICATION_JSON);
        header.setAccept((List<MediaType>) APPLICATION_JSON);
        // headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity entity = new HttpEntity(header); // post data 함께


        String url = "http://127.0.0.1:8080/test";
        ResponseEntity<UserVO[]> results = restTemplate.exchange(url, HttpMethod.GET, entity, UserVO[].class);
        List<UserVO> users = Arrays.asList(results.getBody());
        log.info(String.valueOf(users.size()));

    }

    public void basicPost() throws IOException {
        // requestFactory 없어도됨?

        String url = "http://127.0.0.1:8080/test";

        HttpHeaders header = new HttpHeaders();
        header.add("Content-Type", "application/json; utf-8");
        header.add("Accept", "application/json");

        UserVO user1 = new UserVO(1L, "first", 20, "1@naver.com", "0000");
        UserVO user2 = new UserVO(2L, "second", 20, "2@naver.com", "0000");
        List<UserVO> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        // json으로 변경 안 해도됨?

        HttpEntity entity = new HttpEntity(userList, header);

        ResponseEntity<UserVO> result = restTemplate.exchange(url, HttpMethod.POST, entity, UserVO.class);
        UserVO user = result.getBody();
        log.info(user.getName());


    }


}
