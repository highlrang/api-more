package coding.json.training.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
@Slf4j
public class AsyncConfig {

    @Bean(name = "threadPoolTaskExecutor")
    public Executor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(100);
        taskExecutor.setMaxPoolSize(100);
        // taskExecutor.setQueueCapacity(10);
        taskExecutor.setThreadNamePrefix("Executor-");
        taskExecutor.setAllowCoreThreadTimeOut(true);
        taskExecutor.initialize();
        return taskExecutor;
    }

    /*
    2021-07-23 22:53:17.426  INFO 5932 --- [   Executor-100] coding.json.practice.service.NewService  : position : 50
2021-07-23 22:53:17.436  INFO 5932 --- [    Executor-99] coding.json.practice.service.NewService  : friend : 50
2021-07-23 22:53:17.436  INFO 5932 --- [    Executor-99] coding.json.practice.service.NewService  : friend : 51
2021-07-23 22:53:17.436  INFO 5932 --- [    Executor-99] coding.json.practice.service.NewService  : position : 51
2021-07-23 22:53:17.441  INFO 5932 --- [    Executor-98] coding.json.practice.service.NewService  : position : 49
2021-07-23 22:53:17.443  INFO 5932 --- [    Executor-97] coding.json.practice.service.NewService  : friend : 49
2021-07-23 22:53:17.443  INFO 5932 --- [    Executor-97] coding.json.practice.service.NewService  : friend : 52
2021-07-23 22:53:17.443  INFO 5932 --- [    Executor-97] coding.json.practice.service.NewService  : position : 52
2021-07-23 22:53:17.447  INFO 5932 --- [    Executor-96] coding.json.practice.service.NewService  : position : 48
2021-07-23 22:53:17.451  INFO 5932 --- [    Executor-95] coding.json.practice.service.NewService  : friend : 48
2021-07-23 22:53:17.451  INFO 5932 --- [    Executor-95] coding.json.practice.service.NewService  : friend : 53
2021-07-23 22:53:17.451  INFO 5932 --- [    Executor-95] coding.json.practice.service.NewService  : position : 53
2021-07-23 22:53:17.453  INFO 5932 --- [    Executor-94] coding.json.practice.service.NewService  : position : 47
2021-07-23 22:53:17.456  INFO 5932 --- [    Executor-93] coding.json.practice.service.NewService  : friend : 47
2021-07-23 22:53:17.456  INFO 5932 --- [    Executor-93] coding.json.practice.service.NewService  : friend : 54
2021-07-23 22:53:17.456  INFO 5932 --- [    Executor-93] coding.json.practice.service.NewService  : position : 54
2021-07-23 22:53:17.459  INFO 5932 --- [    Executor-92] coding.json.practice.service.NewService  : position : 46
2021-07-23 22:53:17.460  INFO 5932 --- [    Executor-91] coding.json.practice.service.NewService  : friend : 46
2021-07-23 22:53:17.460  INFO 5932 --- [    Executor-91] coding.json.practice.service.NewService  : friend : 55
2021-07-23 22:53:17.460  INFO 5932 --- [    Executor-91] coding.json.practice.service.NewService  : position : 55
2021-07-23 22:53:17.462  INFO 5932 --- [    Executor-90] coding.json.practice.service.NewService  : position : 45
2021-07-23 22:53:17.463  INFO 5932 --- [    Executor-89] coding.json.practice.service.NewService  : friend : 45
2021-07-23 22:53:17.464  INFO 5932 --- [    Executor-89] coding.json.practice.service.NewService  : friend : 56
2021-07-23 22:53:17.464  INFO 5932 --- [    Executor-89] coding.json.practice.service.NewService  : position : 56

     */
}