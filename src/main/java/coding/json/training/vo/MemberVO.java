package coding.json.training.vo;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class MemberVO {

    private Long id;
    private String email;
    private String phone_number;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Interests interests;

    // private String[]? ArrayList?


    @Builder
    public MemberVO(Long id, String phone_number, String email){
        this.id = id;
        this.phone_number = phone_number;
        this.email = email;
    }
}

