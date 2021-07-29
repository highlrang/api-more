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
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.batch.runtime.BatchStatus;
import javax.persistence.EntityManagerFactory;

import java.time.LocalDateTime;

import static coding.json.training.domain.QMember.*;
import static org.junit.jupiter.api.Assertions.*;
import static coding.json.training.domain.QMember.member;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest// (classes = {BatchTestConfig.class, QuerydslNoOffsetPagingItemReaderConfig.class})
@RunWith(SpringRunner.class)
@SpringBatchTest // JobLauncherTestUtils ...
public class BatchTest {

    @Autowired EntityManagerFactory emf;
    @Autowired JobLauncherTestUtils jobLauncherTestUtils;
    @Autowired Job job;

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
    public void Job실행() throws Exception {

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("chunkSize", "5")
                .addString("email", "@")
                .addString("datetime", LocalDateTime.now().toString())
                .toJobParameters();

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

    }
}