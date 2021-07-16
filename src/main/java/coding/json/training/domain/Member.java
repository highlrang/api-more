package coding.json.training.domain;


import coding.json.training.domain.dept.Department;
import coding.json.training.domain.dept.Position;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
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

    public void addDepartment(Department department){
        this.department = department;
        department.getMembers().add(this);
    }



}
