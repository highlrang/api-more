package coding.json.practice.batch.jobs;

import coding.json.practice.batch.jobs.process.Option;
import coding.json.practice.batch.jobs.process.QuerydslNoOffsetPagingItemReader;
import coding.json.training.domain.Member;
import com.querydsl.core.types.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.batch.runtime.BatchStatus;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static coding.json.training.domain.QMember.member;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
@SpringBatchTest // JobLauncherTestUtils ...
public class BatchTest {

    @Autowired EntityManagerFactory entityManagerFactory;
    @Autowired EntityManager entityManager;
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
                        entityManagerFactory, pageSize, option, queryFactory
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
    public void QuerydslNoOffsetPagingJob실행() throws Exception {

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("email", "@")
                .addString("datetime", LocalDateTime.now().toString())
                .toJobParameters();

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

    }

    @Test
    public void Job실행() throws Exception{ // cursor, paging, listWriter, QuerydslPaging

        jobLauncherTestUtils.launchJob(
                new JobParametersBuilder()
                        .addString("joinDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                        .toJobParameters()
        );

    }

}