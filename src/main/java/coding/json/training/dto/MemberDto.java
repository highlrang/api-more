package coding.json.training.dto;

import coding.json.training.domain.Member;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MemberDto {

    private Long id;
    private String email;
    private String phone_number;

    // api
    private Grade grade;
    private Integer activityIndex;

    // api
    private Integer age;
    private String job;

    // community
    private List<Interests> interests = new ArrayList<>();


    @Builder
    public MemberDto(Member entity){
        this.id = entity.getId();
        this.email = entity.getEmail();
        this.phone_number = entity.getPhone_number();
        this.grade = entity.getGrade();
        this.activityIndex = entity.getActivityIndex();
        this.age = entity.getAge();
        this.job = entity.getJob();
        this.interests = entity.getInterests();
    }
}

