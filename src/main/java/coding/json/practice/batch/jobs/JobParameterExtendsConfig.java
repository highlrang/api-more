package coding.json.practice.batch.jobs;

import coding.json.practice.batch.jobs.process.CreateJobParameter;
import coding.json.training.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JobParameterExtendsConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final CreateJobParameter jobParameter;

    private static final String JOB_NAME = "ParameterExtendsJob";
    private static final int chunkSize = 2;

    @Bean
    public Job parameterJob(){
        return jobBuilderFactory.get(JOB_NAME)
                .start(step())
                .build();

    }

    @Bean
    public Step step(){
        return stepBuilderFactory.get(JOB_NAME + "Step")
                .chunk(chunkSize)
                .reader(reader())
                .writer(writer())
                .build();
    }

    @Bean(JOB_NAME + "jobParameter")
    @JobScope
    public CreateJobParameter jobParameter(@Value("#{jobParameters[createDate]}") String createDate,
                                            @Value("#{jobParameters[status]}") String status){
        return new CreateJobParameter(createDate, status); // 생성자 방식
    }

    @Bean(name = JOB_NAME +"_reader")
    @StepScope
    public JpaPagingItemReader<Member> reader() {

        Map<String, Object> params = new HashMap<>();
        params.put("createDate", jobParameter.getCreateDate());
        params.put("status", jobParameter.getStatus());
        log.info(">>>>>>>>>>> createDate={}, status={}", jobParameter.getCreateDate(), jobParameter.getStatus());

        return new JpaPagingItemReaderBuilder<Member>()
                .name(JOB_NAME +"_reader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .queryString("SELECT p FROM Product p WHERE p.createDate =:createDate AND p.status =:status")
                .parameterValues(params) // params
                .build();
    }

    private JpaItemWriter writer() {
        return new JpaItemWriter();
    }
}
