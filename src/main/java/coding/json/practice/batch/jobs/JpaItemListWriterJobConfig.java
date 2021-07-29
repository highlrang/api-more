package coding.json.practice.batch.jobs;

import coding.json.practice.batch.jobs.process.ItemListProcessor;
import coding.json.practice.batch.jobs.process.JpaItemListWriter;
import coding.json.practice.batch.jobs.entity.Sales;
import coding.json.practice.batch.jobs.entity.Tax;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class JpaItemListWriterJobConfig {

    private final EntityManagerFactory entityManagerFactory;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    // @Bean
    public Job jpaListWriterJob(){
        return jobBuilderFactory.get("itemListJob")
                .start(step())
                // .incrementer(new RunIdIncrementer()) // 동일 파라미터라도 다시 수행하게 해줌 또는 임의의 난수 파라미터 사용
                // 운영에서는 .preventRestart()로 재실행막고, 개발에서는 BuildNumber, DateTime을 파라미터로 재실행 가능하게
                .build();
    }

    private Step step(){
        return stepBuilderFactory.get("itemListStep")
                .<Sales, List<Tax>>chunk(2)
                .reader(reader())
                .processor(processor())
                .writer(writerList())
                .build();
    }

    private JpaPagingItemReader<Sales> reader(){
        JpaPagingItemReader<Sales> reader = new JpaPagingItemReader<>();
        reader.setQueryString("select s from Sales s");
        reader.setEntityManagerFactory(entityManagerFactory);
        return reader;
    }

    private ItemProcessor<Sales, List<Tax>> processor(){
        return new ItemListProcessor();
        // custom해서 리스트 생성
    }

    private JpaItemListWriter<Tax> writerList() {
        // custom해서 List<T> 전달 받음 > 두 개의 Sales를 통해 6개의 tax list가 write됨
        JpaItemWriter<Tax> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);

        return new JpaItemListWriter<>(writer);
    }
}
