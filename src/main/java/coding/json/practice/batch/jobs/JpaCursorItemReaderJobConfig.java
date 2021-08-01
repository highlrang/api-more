package coding.json.practice.batch.jobs;

import coding.json.training.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JpaCursorItemReaderJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    private int chunkSize;
    @Value("${chunkSize:100}")
    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    // @Bean
    public Job jpaCursorJob() {
        return jobBuilderFactory.get("jpaCursorItemReaderJob")
                .start(itemReaderStep())
                .build();
    }

    @Bean
    public Step itemReaderStep() {
        return stepBuilderFactory.get("jpaCursorItemReaderStep")
                .<Member, Member>chunk(chunkSize)
                .reader(cursorItemReader())
                .writer(cursorItemWriter())
                .build();
    }

    @Bean
    public JpaCursorItemReader<Member> cursorItemReader() {
        return new JpaCursorItemReaderBuilder<Member>()
                .name("jpaCursorItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT m FROM Member m")
                .build();
    }

    private ItemWriter<Member> cursorItemWriter() {
        // log를 위해 @Override
        // insert 필요한 경우에는 processor로 객체 넘겨주면 JpaItemWriter가 merge함
        return list -> {
            for (Member m: list) {
                log.info("Member Id={}", m.getId());
            }
        };
    }
}