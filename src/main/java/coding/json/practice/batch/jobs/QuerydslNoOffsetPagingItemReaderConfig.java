package coding.json.practice.batch.jobs;


import coding.json.practice.batch.jobs.process.Option;
import coding.json.practice.batch.jobs.process.QuerydslNoOffsetPagingItemReader;
import coding.json.practice.batch.jobs.process.QuerydslPagingItemReaderJobParameter;
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
import com.querydsl.core.types.Order;
import javax.persistence.EntityManagerFactory;

import static coding.json.training.domain.QMember.member;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class QuerydslNoOffsetPagingItemReaderConfig {
    public static final String JOB_NAME = "querydslNoOffsetPagingReaderJob";

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

    @Bean
    public Job noOffsetJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(step())
                .build();
    }

    @Bean
    public Step step() {
        return stepBuilderFactory.get("querydslNoOffsetPagingReaderStep")
                .<Member, Member>chunk(chunkSize)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public QuerydslNoOffsetPagingItemReader<Member> reader() {
        // 1. No Offset 옵션
        Option options = new Option(member.id, Order.DESC);
        log.info("chunkSize : " + chunkSize);
        log.info("email : " + jobParameter.getEmail());
        // 2. Querydsl
        return new QuerydslNoOffsetPagingItemReader<>(emf, chunkSize, options, queryFactory -> queryFactory
                .selectFrom(member)
                .where(member.email.contains(jobParameter.getEmail())));
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