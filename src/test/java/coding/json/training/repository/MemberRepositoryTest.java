package coding.json.training.repository;

import coding.json.training.domain.Member;
import coding.json.training.domain.dept.Category;
import coding.json.training.domain.dept.Department;
import coding.json.training.domain.dept.Position;
import coding.json.training.domain.dept.PostAdmin;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
class MemberRepositoryTest {

    @Autowired private DepartmentRepository departmentRepository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private PostAdminRepository postAdminRepository;

    @Test
    public void 멤버저장(){
        PostAdmin admin = new PostAdmin(Position.Staff, Category.Exercise);
        Department dept = departmentRepository.save(admin);


        Member member = Member.builder()
                .name("정혜우")
                .email("jhw127@naver.com")
                .build();
        member.addDepartment(dept);
        Member result = memberRepository.save(member);

        PostAdmin after = postAdminRepository.findById(dept.getId()).get();
        assertThat(after.getCategory()).isEqualTo(Category.Exercise);

        assertThat(result.getDepartment().getMembers().size()).isNotEqualTo(0);
        assertThat(result.getDepartment().getPosition()).isEqualTo(Position.Staff);

    }

}