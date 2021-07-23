package coding.json.practice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


// @Autowired로 주입받아서 사용

@Service
@Slf4j
public class NewAsyncService {

    @Async("threadPoolTaskExecutor")
    public void asyncMethod(int i) { // or return CompletableFuture
        String str = "고양이 " + i + "마리";
        if(i/10 == 0){
            str += " ! 띠용 !";
        }
        log.info(str);
    }
}
