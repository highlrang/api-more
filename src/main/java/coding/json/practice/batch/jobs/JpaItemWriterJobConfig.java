package coding.json.practice.batch.jobs;

import coding.json.practice.batch.jobs.entity.Pay;
import coding.json.practice.batch.jobs.entity.Pay2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JpaItemWriterJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    private static final int chunkSize = 10;

    // @Bean
    public Job jpaWriterJob() {
        return jobBuilderFactory.get("jpaItemWriterJob")
                .start(itemWriterStep())
                .build();
    }

    @Bean
    public Step itemWriterStep() {
        return stepBuilderFactory.get("jpaItemWriterStep")
                .<Pay, Pay2>chunk(chunkSize)
                .reader(itemWriterReader())
                .processor(itemProcessor())
                .writer(itemWriter())
                .build();
    }

    @Bean
    public JpaPagingItemReader<Pay> itemWriterReader() {
        return new JpaPagingItemReaderBuilder<Pay>()
                .name("jpaItemWriterReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .queryString("SELECT p FROM Pay p")
                .build();
    }

    @Bean // ItemProcess<I, O> 받을 타입, 내보낼 타입 // 비즈니스 로직, 보통 람다로 많이 구현, lazy 로딩 가능
    public ItemProcessor<Pay, Pay2> itemProcessor() {
        // jpaItemWriter은 entity를 그대로 merge로 반영하기에 중간 과정 필요
        return pay -> new Pay2(pay.getAmount(), pay.getTxName(), pay.getTxDateTime());
    }

    // CompositeItemProcessor >> ItemProcessor간의 체이닝 지원
    @Bean
    public CompositeItemProcessor compositeProcessor() {
        List<ItemProcessor> delegates = new ArrayList<>(2);
        delegates.add(processor1());
        delegates.add(processor2());

        CompositeItemProcessor processor = new CompositeItemProcessor<>();
        processor.setDelegates(delegates);

        return processor;
    }

    public ItemProcessor<Pay, String> processor1() {
        return Pay::getTxName;
    }

    public ItemProcessor<String, String> processor2() {
        return name -> "Pay, txname : "+ name;
    }


    @Bean
    public JpaItemWriter<Pay2> itemWriter() {
        JpaItemWriter<Pay2> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return jpaItemWriter;
    }
}

// Reader와 달리 Writer의 경우 다양한 상황에 맞춰 구현해야함
// 외부API로 전달하거나 비교를 위해 싱글톤 객체에 값을 넣어야 하거나 여러 entity를 동시에 save해야 하거나 등등
// >> 그럴 때 ItemWriter 인터페이스를 구현하면 됨
// ex) 로그 찍을 수 있게 변형 return ItemWriter 조작(람다식으로 기존 writer 메서드 @Override한 것임)
// @Bean
// public ItemWriter<Pay2> customItemWriter() {
//    return items -> {
//      for(Pay2 item : items){
//          log.info(item):
//      }
//    };
//}
