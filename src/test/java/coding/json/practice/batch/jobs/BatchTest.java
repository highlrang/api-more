package coding.json.practice.batch.jobs;

import coding.json.practice.batch.jobs.process.Option;
import coding.json.practice.batch.jobs.process.QuerydslNoOffsetPagingItemReader;
import coding.json.practice.batch.jobs.process.QuerydslPagingItemReaderJobParameter;
import coding.json.training.config.BatchTestConfig;
import coding.json.training.config.QuerydslConfig;
import coding.json.training.domain.Member;
import coding.json.training.domain.QMember;
import com.querydsl.core.types.Order;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.batch.runtime.BatchStatus;
import javax.persistence.EntityManagerFactory;

import static coding.json.training.domain.QMember.*;
import static org.junit.jupiter.api.Assertions.*;
import static coding.json.training.domain.QMember.member;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest //(classes = {BatchTestConfig.class, QuerydslNoOffsetPagingItemReaderConfig.class})
// 그냥 paging도 테스트 해보기
@EnableAutoConfiguration(exclude = {})
@ContextConfiguration(classes = QuerydslNoOffsetPagingItemReaderConfig.class)
@RunWith(SpringRunner.class)
@SpringBatchTest // JobLauncherTestUtils ... //querydslNoOffsetPagingReaderJob noOffsetJob
@TestPropertySource(properties = {"job.name=noOffsetJob", "chunkSize=5", "email=1"})
public class BatchTest {

    @Autowired EntityManagerFactory emf;
    @Autowired JobLauncherTestUtils jobLauncherTestUtils;
    @Autowired JPAQueryFactory jpaQueryFactory;

    @Test
    public void 필드이름(){
        Option option = new Option(member.id, Order.ASC);
        assertThat(option.getFieldName()).isEqualTo("id");
    }

    @Test
    public void 리더실행(){
        int pageSize = 5;
        Option option = new Option(member.id, Order.ASC);

        QuerydslNoOffsetPagingItemReader<Member> reader =
                new QuerydslNoOffsetPagingItemReader<>(
                        emf, pageSize, option, queryFactory
                        -> queryFactory.selectFrom(member).where(member.email.contains("1")));

        reader.open(new ExecutionContext());

        try {
            Member member = reader.read();
            assertTrue(member.getEmail().contains("1"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void 쿼리확인(){
        jpaQueryFactory.select(member.id)
                .from(member)
                .fetch();
    }

    @Test
    public void 잡실행() throws Exception { // class 지정하면 member 인식 안됨

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("chunkSize", "5")
                .addString("email", "1")
                .toJobParameters();


        /*
        QuerydslPagingItemReaderJobParameter jobParameter = new QuerydslPagingItemReaderJobParameter();
        jobParameter.setEmail("1");
        QuerydslNoOffsetPagingItemReaderConfig config
                = new QuerydslNoOffsetPagingItemReaderConfig(jobBuilderFactory, stepBuilderFactory, emf, jobParameter);

         */

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

    }
}