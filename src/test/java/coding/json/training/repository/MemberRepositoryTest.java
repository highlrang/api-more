package coding.json.training.repository;

import coding.json.training.domain.Member;
import coding.json.training.domain.dept.Category;
import coding.json.training.domain.dept.Department;
import coding.json.training.domain.dept.Position;
import coding.json.training.domain.dept.PostAdmin;
import coding.json.training.repository.member.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
class MemberRepositoryTest {

    @Autowired private PostAdminRepository postAdminRepository;
    @Autowired private DepartmentRepository departmentRepository;
    @Autowired private MemberRepository memberRepository;

    @Test
    public void 멤버저장(){
        PostAdmin admin = new PostAdmin(Category.Exercise); // findById
        PostAdmin savedAdmin = (PostAdmin) departmentRepository.save(admin);

        Member member = Member.builder()
                .name("정혜우")
                .email("jhw127@naver.com")
                .build();
        member.addDepartment(savedAdmin);
        Member result = memberRepository.save(member);

        assertThat(savedAdmin.getCategory()).isEqualTo(Category.Exercise);

        assertThat(result.getDepartment().getMembers().size()).isNotEqualTo(0);

    }

    @Test
    public void 신입저장(){
        Member member = Member.builder().name("신입").build();
        PostAdmin postAdmin = postAdminRepository.findAll().get(0);
        member.addDepartment(postAdmin);
        memberRepository.save(member);
    }
}