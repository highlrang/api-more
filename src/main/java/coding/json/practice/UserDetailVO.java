package coding.json.practice;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserDetailVO {

    private String alias;

    public UserDetailVO(String alias){
        this.alias = alias;
    }
}
