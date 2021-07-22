package coding.json.practice.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class UserVO {
    private Long id;
    private String name;
    private Integer age;
    private String email;
    private String phoneNumber;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<UserFriendVO> details = new ArrayList<>();

    // entity에서는 임베디드로
    private PositionVO position;

    public UserVO(Long id, String name, Integer age, String email, String phoneNumber){
        this.id = id;
        this.name = name;
        this.age = age;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }


    // @JsonSetter("details")
    public void addFriends(List<UserFriendVO> details){
        this.details = details;
    }

    public void addPosition(PositionVO position){
        this.position = position;
    }

    public String toString(){
        return "id = " + id + " name = " + name + " details = " + details;
    }
}
