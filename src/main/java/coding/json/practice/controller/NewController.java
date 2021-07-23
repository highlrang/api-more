package coding.json.practice.controller;

import coding.json.practice.service.NewService;
import coding.json.practice.vo.PositionVO;
import coding.json.practice.vo.UserFriendVO;
import coding.json.practice.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


@RestController
@RequiredArgsConstructor
public class NewController {

    private final NewService newService;

    @GetMapping("/api/v1/users")
    public List<UserVO> usersAllInfo() throws ExecutionException, InterruptedException {
        return newService.getAllInfo();
    }

    @GetMapping("/external/api/v1/users")
    public List<UserVO> users(){
        List<UserVO> users = new ArrayList<>();

        for(int i=1; i<101; i++){
            users.add(new UserVO((long) i, i+" 사용자 "+i, 20, i+"@naver.com", "010-0000-0000"));
        }

        return users;
    }

    @GetMapping("/external/api/v1/users/{userId}/friends-info")
    public List<UserFriendVO> friends(@PathVariable("userId") Long userId){

        List<UserFriendVO> friends = new ArrayList<>();

        for(int i=1; i<11; i++) {
            UserVO userFriend = new UserVO(userId+i, userId + "의 친구", 20, userId+i+ "@naver.com", "010-0000-0000");
            UserFriendVO friend = new UserFriendVO(userId, userFriend, "friend");
            friends.add(friend);
        }

        return friends;

    }

    @GetMapping("/external/api/v1/users/{userId}/position-info")
    public PositionVO position(@PathVariable("userId") Long userId){

        Long id = userId * 2;
        PositionVO position = new PositionVO(id, userId, "백엔드", "사원");

        return position;
    }

}
