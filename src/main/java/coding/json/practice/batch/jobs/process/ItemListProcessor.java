package coding.json.practice.batch.jobs.process;

import coding.json.practice.batch.jobs.entity.Sales;
import coding.json.practice.batch.jobs.entity.Tax;
import coding.json.training.domain.Member;
import coding.json.training.domain.Notice;
import coding.json.training.domain.dept.Department;
import coding.json.training.domain.dept.PostAdmin;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemListProcessor implements ItemProcessor<Member, List<Notice>> {

    @Override
    public List<Notice> process(Member member) throws Exception{
        // Arrays.asList(new Tax(item.getTxAmount(), item.getOwnerNo()),
        //               new Tax(),
        //               new Tax());

        List<Notice> notices = new ArrayList<>();

        if(member.getJoinDate() != null && member.getJoinDate().isAfter(LocalDateTime.now().minusMonths(1))){
            notices.add(new Notice("신입사원 메세지", "입사를 축하합니다. " + member.getDepartment() + " 부서의 관련 안내가 전달될 것입니다.", member));
        }

        if(member.getPosition() != null && (member.getPosition().getKey().equals("Staff") || member.getPosition().getKey().equals("SeniorStaff")) ){
            notices.add(new Notice("교육 이수 메세지", "이번 달 교육 일정 안내가 전달될 예정입니다.", member));
        }

        return notices;
    }
}
