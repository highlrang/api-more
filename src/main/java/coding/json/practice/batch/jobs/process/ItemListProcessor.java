package coding.json.practice.batch.jobs.process;

import coding.json.practice.batch.jobs.entity.Sales;
import coding.json.practice.batch.jobs.entity.Tax;
import org.springframework.batch.item.ItemProcessor;

import java.util.Arrays;
import java.util.List;

public class ItemListProcessor implements ItemProcessor<Sales, List<Tax>> {

    @Override
    public List<Tax> process(Sales item) throws Exception{
        return Arrays.asList(
                new Tax(item.getTxAmount(), item.getOwnerNo()),
                new Tax((int)(item.getTxAmount()/1.1), item.getOwnerNo()),
                new Tax(item.getTxAmount()/11, item.getOwnerNo())
        );
    }
}
