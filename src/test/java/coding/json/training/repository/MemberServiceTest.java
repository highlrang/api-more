package coding.json.training.repository;

import coding.json.training.domain.Member;
import coding.json.training.domain.dept.Category;
import coding.json.training.domain.dept.Position;
import coding.json.training.domain.dept.PostAdmin;
import coding.json.training.dto.MemberRequestDto;
import coding.json.training.dto.MemberResponseDto;
import coding.json.training.service.MemberService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired DepartmentRepository departmentRepository;

    @Test
    public void 멤버삭제(){
        PostAdmin admin = new PostAdmin(Category.Exercise); // findById
        PostAdmin savedAdmin = (PostAdmin) departmentRepository.save(admin);

        MemberRequestDto memberRequestDto = new MemberRequestDto("정혜우", "jhw127@naver.com", admin);
        Long id = memberService.save(memberRequestDto);
        MemberResponseDto memberResponseDto = memberService.findById(id);


        // when
        memberService.delete(id);

        // then
        assertThrows(IllegalStateException.class,
                () -> memberService.findById(id));

        assertThat(memberResponseDto.getDepartment().getMembers().size()).isEqualTo(0);
    }
}
