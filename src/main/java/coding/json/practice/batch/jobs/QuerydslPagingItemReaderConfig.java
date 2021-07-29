package coding.json.practice.batch.jobs;

import coding.json.practice.batch.jobs.process.QuerydslPagingItemReader;
import coding.json.training.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import static coding.json.training.domain.QMember.member;
import coding.json.practice.batch.jobs.process.QuerydslPagingItemReaderJobParameter;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class QuerydslPagingItemReaderConfig {

    public static final String JOB_NAME = "querydslPagingReaderJob";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory emf;
    private final QuerydslPagingItemReaderJobParameter jobParameter;

    private int chunkSize;

    @Value("${chunkSize:1000}")
    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    @Bean
    @JobScope
    public QuerydslPagingItemReaderJobParameter jobParameter() {
        return new QuerydslPagingItemReaderJobParameter();
    }

    //@Bean
    public Job querydslJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(step())
                .build();
    }

    // @Bean
    public Step step() {
        return stepBuilderFactory.get("querydslPagingReaderStep")
                .<Member, Member>chunk(chunkSize)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    // @Bean // reader에서 custom한 QuerydslItemReader 사용
    public QuerydslPagingItemReader<Member> reader() {
        return null;
        // new QuerydslPagingItemReader<>()(emf, chunkSize, queryFactory -> queryFactory.selectFrom(member));
                // .where(product.createDate.eq(jobParameter.getTxDate())));
    }

    private ItemProcessor<Member, Member> processor() {
        return Member::new;
    }

    @Bean
    public JpaItemWriter<Member> writer() {
        return new JpaItemWriterBuilder<Member>()
                .entityManagerFactory(emf)
                .build();
    }
}