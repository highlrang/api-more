package coding.json.practice.batch.jobs;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Batch > Job > Step > Tasklet // 동일한 파라미터 실행 안 함

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SimpleJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job simpleJob(){
        return jobBuilderFactory.get("simpleTaskletJob")
                .start(simpleStep1(null))
                .next(simpleStep2(null))
                .build();
    }

    @Bean
    @JobScope // Step에서 사용 가능
    // @StepScope >> jobParameters, jobExecutionContext, stepExecutionContext
    // Tasklet, ItemReader-ItemWriter 등에서 사용 가능
    // @Bean과 함께 사용할 경우 proxy 모드 (read만 가능)
    // >> ItemReader 등이 아닌 실제 return 객체를 명시하기 (JpaPagingItemReader<T>)
    public Step simpleStep1(@Value("#{jobParameters[requestDate]}") String requestDate){
        return stepBuilderFactory.get("simpleStep1")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>> Step1, requestDate = {}", requestDate);
                    return RepeatStatus.FINISHED;
                    // throw new IllegalArgumentException("step1에서 실패");
                })
                .build();
    }

    @Bean
    @JobScope
    public Step simpleStep2(@Value("#{jobParameters[requestDate]}") String requestDate){
        return stepBuilderFactory.get("simpleStep2")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>> Step2, requestDate = {}", requestDate);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    // job name 지정 > yml에 spring.batch.job.names: ${job.name:NONE}
    // 실행 > java -jar batch-application.jar --job.name=simpleJob
}
