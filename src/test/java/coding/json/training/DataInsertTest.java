package coding.json.training;

import coding.json.training.domain.Member;
import coding.json.training.domain.dept.Category;
import coding.json.training.domain.dept.Position;
import coding.json.training.domain.dept.PostAdmin;
import coding.json.training.repository.DepartmentRepository;
import coding.json.training.repository.member.MemberQueryRepository;
import coding.json.training.repository.member.MemberQuerydslRepository;
import coding.json.training.repository.member.MemberRepository;
import coding.json.training.repository.PostAdminRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class DataInsertTest {

    @Autowired MemberRepository memberRepository;
    @Autowired MemberQueryRepository memberQueryRepository;
    @Autowired DepartmentRepository departmentRepository;
    @Autowired PostAdminRepository postAdminRepository;
    @Autowired MemberQuerydslRepository memberQuerydslRepository;

    @Test @Commit
    public void 멤버저장(){

        Category[] categories = Category.values();
        Category category;
        for(int i=1; i<21; i++){
            if(i>5) {
                category = categories[i%5];
            }else{
                category = categories[i];
            }

            PostAdmin postAdmin = postAdminRepository.findByCategory(category).get();

            Member member = Member.builder().email("email"+i+"@email.com").name(category.toString()+"게시판관리자"+i).build();
            member.addDepartment(postAdmin);
            memberRepository.save(member);

        }

        List<Member> members = memberRepository.findAll();
        List<PostAdmin> postAdmins = (List<PostAdmin>) departmentRepository.findAll();
        System.out.println(members.size() + members.get(0).getName());
        for(PostAdmin p: postAdmins) System.out.println(p.getCategory().name());



    }

    @Test
    public void 이누머레이트(){
        Position p = Position.AssistantManager;
        System.out.println(p.getKey());
    }

}
