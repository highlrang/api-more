package coding.json.practice.batch.jobs;


import coding.json.practice.batch.jobs.process.Option;
import coding.json.practice.batch.jobs.process.QuerydslNoOffsetPagingItemReader;
import coding.json.practice.batch.jobs.process.QuerydslPagingItemReaderJobParameter;
import coding.json.training.domain.Member;
import coding.json.training.dto.MemberResponseDto;
import com.querydsl.jpa.JPAExpressions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.querydsl.core.types.Order;
import javax.persistence.EntityManagerFactory;

import static coding.json.training.domain.QMember.member;
import static coding.json.training.domain.dept.QDepartment.department;
import static coding.json.training.domain.dept.QPostAdmin.postAdmin;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class QuerydslNoOffsetPagingItemReaderConfig {
    public static String JOB_NAME = "querydslNoOffsetPagingReaderJob";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory emf;
    private final QuerydslPagingItemReaderJobParameter jobParameter;

    private int chunkSize;
    @Value("${chunkSize:1000}")
    public void setChunkSize(int chunkSize) {
        log.info("chunkSize = " + chunkSize);
        this.chunkSize = chunkSize;
    }

    @Bean
    @JobScope
    public QuerydslPagingItemReaderJobParameter jobParameter() { // DI
        return new QuerydslPagingItemReaderJobParameter();
    }

    @Bean
    public Job noOffsetJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(noOffsetStep())
                .build();
    }

    @Bean
    public Step noOffsetStep() {
        return stepBuilderFactory.get("querydslNoOffsetPagingReaderStep")
                .<Member, Member>chunk(chunkSize)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    // reader끼리 같은 빈 아닌데 extends 오버라이딩했다고 에러, config에 상위 클래스랑 같은 빈 사용하라고함
    // >> 상위 모델 return으로 했더니 오버라이드 안되고 상위 클래스 사용됨
    // >> 스텝이 아예 그쪽으로 실행되어서 빈 지워줌 ... (== 빈 문제였음)
    @Bean
    @StepScope
    public QuerydslNoOffsetPagingItemReader<Member> reader() {
        // 1. No Offset 옵션
        Option options = new Option(member.id, Order.DESC);
        log.info("한달 동안의 신입사원을 위한 기준 날짜 = " + jobParameter.getJoinDate());

        // 2. Querydsl
        return new QuerydslNoOffsetPagingItemReader<>(emf, chunkSize, options, queryFactory -> queryFactory
                .selectFrom(member)
                .join(member.department, department)
                .where(department.joinDate.after(jobParameter.getJoinDate()),
                        department.id.in(
                                JPAExpressions
                                        .select(postAdmin.id)
                                        .from(postAdmin)
                        )
                        ));
    }

    private ItemProcessor<Member, Member> processor() {
        return Member::new; // 새로운 entity 생성 시 자동으로 merge함
    }

    @Bean
    public JpaItemWriter<Member> writer() {
        return new JpaItemWriterBuilder<Member>()
                .entityManagerFactory(emf)
                .build();
    }
}