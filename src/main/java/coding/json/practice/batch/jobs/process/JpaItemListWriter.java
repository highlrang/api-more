package coding.json.practice.batch.jobs.process;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.database.JpaItemWriter;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class JpaItemListWriter<T> extends JpaItemWriter<List<T>> { // T == List<T>
    private JpaItemWriter<T> jpaItemWriter;

    public JpaItemListWriter(JpaItemWriter<T> jpaItemWriter) {
        this.jpaItemWriter = jpaItemWriter;
    }

    @Override
    public void write(List<? extends List<T>> items){ // 이중 리스트 넘겨받음
        List<T> totalList = new ArrayList<>();

        for(List<T> list : items){
            totalList.addAll(list);
            log.info("write 아닌 logging = " + list.toArray());
        }

        // write
        jpaItemWriter.write(totalList); // merge

    }
}
