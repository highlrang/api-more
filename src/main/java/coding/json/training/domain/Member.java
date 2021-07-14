package coding.json.training.domain;


import coding.json.training.dto.Grade;
import coding.json.training.dto.Interests;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String phone_number;

    private Grade grade;
    private Integer activityIndex;

    private Integer age;
    private String job;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Interests> interests = new ArrayList<>();

    @Builder
    public Member(Long id, String email, String phone_number){
        this.id = id;
        this.email = email;
        this.phone_number = phone_number;
    }
}
