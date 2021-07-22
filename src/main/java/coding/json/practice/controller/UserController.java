package coding.json.practice.controller;

import coding.json.practice.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class UserController {

    @Async("threadPoolTaskExecutor")
    @GetMapping("/test/{id}")
    public UserVO test(){
        return new UserVO();
    }

    @GetMapping("/test")
    public List<UserVO> testGet(){

        UserVO user1 = new UserVO(1, "GET method test ...");
        UserVO user2 = new UserVO(2, "Second Data in List Test");
        List<UserVO> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        return users;
    }

    @PostMapping("/test")
    public List<UserVO> testPost(@RequestBody List<UserVO> users){
        for(UserVO u: users) log.info(this.getClass() + "POST METHOD 통과했습니다 : "+u.getName());
        return users;
    }

}
