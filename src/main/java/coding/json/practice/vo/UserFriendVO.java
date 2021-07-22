package coding.json.practice.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserFriendVO {

    private Long userId;
    private UserVO friendInfo;
    private String relation; // enum

    public UserFriendVO(Long userId, UserVO friendInfo, String relation){
        this.userId = userId;
        this.friendInfo = friendInfo;
        this.relation = relation;
    }
}
