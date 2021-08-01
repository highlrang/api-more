package coding.json.training.domain;


import coding.json.training.domain.dept.Department;
import coding.json.training.dto.PostAdminDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
/*
@SqlResultSetMapping(
        name = "BestPostAdminMapping",
        entities = {
                @EntityResult(
                        entityClass = Member.class,
                        fields = { @FieldResult(name = "id", column = "p.id") }
                )
        }
)
*/
@SqlResultSetMapping(
        name = "PostAdminMapping",
        classes = @ConstructorResult(
                targetClass = PostAdminDto.class,
                columns = {
                        @ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "name"),
                        @ColumnResult(name = "category")
                }
        )
)
public class Member { // manager administrator

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    private Department department;

    @Builder
    public Member(String name, String email){
        this.name = name;
        this.email = email;
    }

    public Member(Member member) {
        id = member.getId();
        name = member.getName();
        email = member.getEmail();
        department = member.getDepartment();
    }

    public void addDepartment(Department department){
        this.department = department;
        department.getMembers().add(this);
    }

    public void deleteDepartment(){
        department.getMembers().remove(this);
    }



}
