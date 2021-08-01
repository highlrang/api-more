package coding.json.practice.batch.jobs;

import coding.json.practice.batch.jobs.entity.Pay;
import coding.json.training.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;


// Reader와 Processor(chunkProvider.provide())에서는 1건씩 다뤄지고(add), Writer(chunkProvider.process())에선 Chunk 단위로 처리
// Chunk Size는 한번에 처리될 트랜잭션 단위를 얘기하며, Page Size는 한번에 조회할 Item의 양
// Tasklet 중에서 ChunkOrientedTasklet을 통해 Chunk를 처리하며 이를 구성하는 3 요소로 ItemReader, ItemWriter, ItemProcessor

// cursor 기반과 paging 기반
// (전자는 ResultSet과 직접 연동하여 데이터를 읽어오는 것인데, 일종의 stream
// 전체를 조회하여 가져오는 방식이기에 페이징 이슈는 발생하지 않음, PagingItemReader보다 성능 좋음
// 후자는 대량의 데이터, 멀티쓰레드 환경에서 사용.
@Slf4j
@RequiredArgsConstructor
@Configuration
public class JpaPagingItemReaderJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    private final static int chunkSize = 10;

    // @Bean
    public Job jpaPagingJob(){
        return jobBuilderFactory.get("jpaPagingItemReaderJob")
                .start(pagingItemReaderStep())
                .build();
    }

    @Bean
    public Step pagingItemReaderStep(){
        return stepBuilderFactory.get("jpaPagingItemReaderStep")
                .<Member, Member>chunk(chunkSize)
                .reader(pagingItemReader())
                .writer(pagingItemWriter())
                .build();
    }

    @Bean
    public JpaPagingItemReader<Member> pagingItemReader(){ // RepositoryItemReader
        return new JpaPagingItemReaderBuilder<Member>()
                .name("jpaPagingItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .queryString("select m from Member m") // order by 필요 !!
                .build();
    }

    private ItemWriter<Member> pagingItemWriter(){ // list(chunk)
        return list -> {
            for (Member member:list){
                log.info("Current memberId = {}", member.getId());
            }
        };
    }

}
