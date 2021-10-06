package coding.json.training.domain.dept;

import coding.json.training.domain.Member;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// @MappedSuperclass는 부모 변수를 받아서 실제로 상속받은 자식 클래스를 사용, @Embedded도 있음
@Inheritance(strategy= InheritanceType.SINGLE_TABLE) // 부모 지정해두고 enum처럼 골라서 사용 가능
// postAdmin 만들고 member deparment에 그거 넣어주면 됨
@DiscriminatorColumn(name="dept") // DiscriminatorColumn <> Value 개별 이름
@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter
public abstract class Department {
    // HumanResource, Design, Marketing, Developer, CustomerService extends Department

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    private List<Member> members = new ArrayList<>();

}
