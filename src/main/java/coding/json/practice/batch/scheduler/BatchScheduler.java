package coding.json.practice.batch.scheduler;

import coding.json.practice.batch.jobs.QuerydslNoOffsetPagingItemReaderConfig;
import coding.json.practice.batch.jobs.process.QuerydslPagingItemReaderJobParameter;
import coding.json.training.domain.Member;
import coding.json.training.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
@Slf4j
public class BatchScheduler {

    private final MemberRepository memberRepository;
    private final JobLauncher jobLauncher;
    @Autowired Job job;

    @Scheduled(cron = "0 */1 * * * *")
    public void executeJob () {
        try {
            log.info(String.format("---------------%s job 실행 전----------------", job.getName()));

            String beforeAMonth = LocalDateTime.now().minusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            log.info("BeforeAMonth = " + beforeAMonth);

            jobLauncher.run(
                    job,
                    new JobParametersBuilder()
                            .addString("datetime", LocalDateTime.now().toString())
                            .addString("joinDate", beforeAMonth)
                            .toJobParameters()
            );

            log.info(String.format("---------------%s job 실행 후----------------", job.getName()));

        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

}