package coding.json.practice;

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
@Setter
public class UserVO {
    private Integer id;
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<UserDetailVO> details = new ArrayList<>();


    public UserVO(Integer id, String name){
        this.id = id;
        this.name = name;
    }

    /* setter 있어도 되나?
    @JsonSetter("details")
    public void addDetails(List<UserDetailVO> details){
        this.details = details;
    }
     */


    public String toStr(UserVO user){
        return "id = " + user.getId() + " name = " + user.getName() + " details = " + user.getDetails().size();
    }
}
