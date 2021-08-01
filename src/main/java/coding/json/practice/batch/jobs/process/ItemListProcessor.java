package coding.json.practice.batch.jobs.process;

import coding.json.practice.batch.jobs.entity.Sales;
import coding.json.practice.batch.jobs.entity.Tax;
import coding.json.training.domain.Member;
import coding.json.training.domain.dept.Department;
import org.springframework.batch.item.ItemProcessor;

import java.util.Arrays;
import java.util.List;

public class ItemListProcessor implements ItemProcessor<Department, List<Member>> {

    @Override
    public List<Member> process(Department department) throws Exception{
        // Arrays.asList(new Tax(item.getTxAmount(), item.getOwnerNo()), ...);
        return department.getMembers();
    }
}
