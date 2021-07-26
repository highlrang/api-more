package coding.json.practice.batch.jobs.process;

import org.springframework.batch.item.database.JpaItemWriter;

import java.util.ArrayList;
import java.util.List;

public class JpaItemListWriter<T> extends JpaItemWriter<List<T>> { // T == List<T>
    private JpaItemWriter<T> jpaItemWriter;

    public JpaItemListWriter(JpaItemWriter<T> jpaItemWriter) {
        this.jpaItemWriter = jpaItemWriter;
    }

    @Override
    public void write(List<? extends List<T>> items){
        List<T> totalList = new ArrayList<>();
        for(List<T> list : items){
            totalList.addAll(list);
        }
        jpaItemWriter.write(totalList);
    }
}
