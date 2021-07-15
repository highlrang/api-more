package coding.json.training.dto;



import coding.json.training.domain.Member;
import coding.json.training.domain.dept.Department;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberRequestDto {

    private Long id;
    private String name;
    private String email;
    private Department department;


    public MemberRequestDto(String name, String email, Department department){
        this.name = name;
        this.email = email;
        this.department = department;
    }

    public Member toMember(MemberRequestDto m){
        Member member = Member.builder()
                .name(m.getName())
                .email(m.getEmail())
                .build();
        member.addDepartment(m.getDepartment());
        return member;
    }
}
