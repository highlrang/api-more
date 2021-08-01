package coding.json.practice.batch.jobs;

import coding.json.practice.batch.jobs.process.ItemListProcessor;
import coding.json.practice.batch.jobs.process.JpaItemListWriter;
import coding.json.practice.batch.jobs.entity.Sales;
import coding.json.practice.batch.jobs.entity.Tax;
import coding.json.training.domain.Member;
import coding.json.training.domain.dept.Department;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class JpaItemListWriterJobConfig {

    private final EntityManagerFactory entityManagerFactory;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    // @Bean
    public Job jpaListWriterJob(){
        return jobBuilderFactory.get("itemListJob")
                .start(itemListStep())
                // .incrementer(new RunIdIncrementer()) // 동일 파라미터라도 다시 수행하게 해줌 또는 임의의 난수 파라미터 사용
                // 운영에서는 .preventRestart()로 재실행막고, 개발에서는 BuildNumber, DateTime을 파라미터로 재실행 가능하게
                .build();
    }

    private Step itemListStep(){
        return stepBuilderFactory.get("itemListStep")
                .<Department, List<Member>>chunk(2)
                .reader(itemListReader())
                .processor(itemListProcessor())
                .writer(itemListWriter())
                .build();
    }

    private JpaPagingItemReader<Department> itemListReader(){
        JpaPagingItemReader<Department> reader = new JpaPagingItemReader<>();
        reader.setQueryString("select d from Department d join fetch d.members m");
        reader.setEntityManagerFactory(entityManagerFactory);
        return reader;
    }

    private ItemProcessor<Department, List<Member>> itemListProcessor(){
        return new ItemListProcessor();
        // custom해서 return 리스트 생성
    }

    private JpaItemListWriter<Member> itemListWriter() {
        // custom해서 T 자리에 List<T> 전달 받음 > 두 개의 Sales를 통해 6개의 tax list가 write됨
        JpaItemWriter<Member> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);

        return new JpaItemListWriter<>(writer);
    }

}
