package coding.json.practice.batch.scheduler;

import coding.json.practice.batch.jobs.QuerydslNoOffsetPagingItemReaderConfig;
import coding.json.practice.batch.jobs.process.QuerydslPagingItemReaderJobParameter;
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

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class BatchScheduler {

    @Autowired
    private final QuerydslNoOffsetPagingItemReaderConfig job;
    private final JobLauncher jobLauncher;

    @Scheduled(cron = "0 */1 * * * *")
    public void executeJob () {
        try {
            log.info("---------------job 실행 전----------------");

            jobLauncher.run(
                    job.noOffsetJob(),
                    // new JobParametersBuilder().toJobParameters()
                    new JobParametersBuilder()
                            .addString("datetime", LocalDateTime.now().toString())
                            .addString("email", "@")
                            .addString("chunk", "10")
                            // .addJobParameters(new JobParameters())
                            .toJobParameters()
            );




            log.info("---------------job 실행 후----------------");
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

}