package coding.json.practice.service;

import coding.json.practice.vo.PositionVO;
import coding.json.practice.vo.UserFriendVO;
import coding.json.practice.vo.UserVO;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.InjectService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewService {

    private final WebClient webClient = (WebClient) WebClient.create()
            .mutate().defaultHeaders(httpHeaders -> {
                httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                httpHeaders.setAccept((List<MediaType>) MediaType.APPLICATION_JSON);
            })
            .baseUrl("http://127.0.0.01:9090/external/api/v1/users");

    // 에러 처리 추가하기

    public List<UserVO> getAllInfo(){

        List<UserVO> users = (List<UserVO>) findAllUsers();

        List<Long> userIds = users.stream()
                .map(UserVO::getId)
                .collect(Collectors.toList());

        Map<Long, List<UserFriendVO>> friends = (Map<Long, List<UserFriendVO>>) findUserDetails(userIds);
        Map<Long, PositionVO> positions = (Map<Long, PositionVO>) findPositions(userIds);

        // forEach문 비동기로 해야하나? 유의미한 성능 개선인지 확인하기
        users.forEach(u -> {u.addFriends(friends.get(u.getId())); u.addPosition(positions.get(u.getId())); });

        return users;
    }

    @Async("threadPoolTaskExecutor")
    public CompletableFuture findAllUsers() {

        List<UserVO> users = new ArrayList<>();

        Mono<UserVO[]> mono = webClient.mutate()
                .build()
                .get()
                .retrieve()
                .bodyToMono(UserVO[].class);

        users.addAll(Arrays.asList(mono.block()));

        return CompletableFuture.completedFuture(users);
    }

    @Async("threadPoolTaskExecutor")
    public CompletableFuture findUserDetails(List<Long> userIds){
        Map<Long, List<UserFriendVO>> userFriends = new HashMap<>();
        // String baseUrl = "http://127.0.0.01:9090/external/api/v1/users/";

        for(Long userId : userIds) {

            Mono<UserFriendVO[]> mono = webClient.mutate()
                    .build()
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/")
                            .queryParam("userId", userId)
                            .path("/friends-info")
                            .build()
                    )
                    .retrieve()
                    .bodyToMono(UserFriendVO[].class);

            List<UserFriendVO> friends = Arrays.asList(mono.block());
            userFriends.put(userId, friends);

        }

        return CompletableFuture.completedFuture(userFriends);
    }

    @Async("threadPoolTaskExecutor")
    public CompletableFuture findPositions(List<Long> userIds){

        Map<Long, PositionVO> positionMap = new HashMap<>();

        for(Long userId : userIds){
            Mono<PositionVO> mono = webClient.mutate()
                    .build()
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/")
                            .queryParam("userId", userId)
                            .path("/position-info")
                            .build()
                    ).retrieve()
                    .bodyToMono(PositionVO.class);

            positionMap.put(userId, mono.block());

        }

        return CompletableFuture.completedFuture(positionMap);
    }


    /* webClient 사용법
    // create
    WebClient
            .create()
            .get()
            .uri(uriBuilder -> uriBuilder.path("/")
                .queryParam("", "")
                .build()
            )
            .retrieve();

    // builder
    WebClient.builder()
            .baseUrl(url)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeaders(httpHeaders -> {
                httpHeaders.add("user-agent", "WebClient")
            })
            .build();

    // httpClient
    HttpClient httpClient = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .responseTimeout(Duration.ofMillis(5000))
            .doOnConnected(conn ->
                    conn.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                    .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS))
            );

    WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .baseUrl("")
            .build()
            .post()
            .uri("1")
            .bodyValue(UserVO.class) // or body BodyInserters.fromFormData(params) fromObject(object)
            .retrieve();
    webClient.post().body(BodyInserters.fromFormData((MultiValueMap<String, String>) .. )).retrieve();
    webClient.post().bodyValue(new PositionVO()).retrieve();

    Mono<UserVO> user = WebClient.create().get()
            .uri("", 1)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve() // retrieve는 body만 가져옴 <> exchange
            .onStatus(HttpStatus::is4xxClientError, __ -> Mono.error(new IllegalArgumentException("4xx")))
            .onStatus(HttpStatus::is5xxServerError, __ -> Mono.error(new IllegalArgumentException("5xx")))
            .bodyToMono(UserVO.class);
            // .toEntity(UserVO.class); ResponseEntity

    UserVO userVO = user.block();

    */
}
