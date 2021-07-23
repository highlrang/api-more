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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilderFactory;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewService {

    private final WebClient webClient = WebClient.create();
    // private static final int THREAD_POOL_SIZE = 100;
    // private final Executor executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    @Autowired private Executor threadPoolTaskExecutor; // bean
    @Autowired private NewAsyncService asyncService;

    public List<UserVO> getAllInfo() throws ExecutionException, InterruptedException {
        for(int i=1; i<51; i++) asyncService.asyncMethod(i);

        List<UserVO> users = findAllUsers();
        List<Long> userIds = users.stream()
                .map(UserVO::getId)
                .collect(Collectors.toList());

        return asyncMethod(users, userIds);

    }


    public List<UserVO> asyncMethod(List<UserVO> users, List<Long> userIds) throws ExecutionException, InterruptedException {

        Map<Long, List<UserFriendVO>> friends = new HashMap<>();
        Map<Long, PositionVO> positions = new HashMap<>();

        for(Long userId : userIds) {
            CompletableFuture.supplyAsync(() -> friends.put(userId, findUserFriends(userId)), threadPoolTaskExecutor);
            CompletableFuture.supplyAsync(() -> positions.put(userId, findPosition(userId)), threadPoolTaskExecutor);
        }

        users.forEach(u -> {u.addFriends(friends.get(u.getId())); u.addPosition(positions.get(u.getId())); });

        return users;
    }

    public List<UserVO> findAllUsers() {

        Mono<UserVO[]> mono = webClient.mutate()
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                    httpHeaders.add("Accept", "application/json");
                })
                .baseUrl("http://127.0.0.1:9090/external/api/v1/users")
                .build()
                .get()
                .retrieve()
                .bodyToMono(UserVO[].class);

        List<UserVO> users = Arrays.asList(mono.block());

        return users;
    }


    public List<UserFriendVO> findUserFriends(Long userId){

        Mono<UserFriendVO[]> mono = webClient.mutate()
                    .defaultHeaders(httpHeaders -> {
                        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                        httpHeaders.add("Accept", "application/json");
                    })
                    .baseUrl("http://127.0.0.1:9090/external/api/v1/users")
                    .build()
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/{userId}/friends-info")
                            // .queryParam("userId", userId)
                            .build(userId)
                    )
                    .retrieve()
                    .bodyToMono(UserFriendVO[].class);

        List<UserFriendVO> friends = Arrays.asList(mono.block());
        return friends;
    }


    public PositionVO findPosition(Long userId){

        Mono<PositionVO> mono = webClient.mutate()
                    .defaultHeaders(httpHeaders -> {
                        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                        httpHeaders.add("Accept", "application/json");
                    })
                    .baseUrl("http://127.0.0.1:9090/external/api/v1/users")
                    .build()
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/{userId}/position-info")
                            // .queryParam("userId", userId)
                            .build(userId)
                    ).retrieve()
                    .bodyToMono(PositionVO.class);

        PositionVO position = mono.block();
        return position;
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
