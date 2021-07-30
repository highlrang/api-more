package coding.json.practice.batch.jobs.process;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Getter
@Slf4j
@NoArgsConstructor
public class QuerydslPagingItemReaderJobParameter {

    @Value("#{jobParameters[email]}")
    private String email;

    private LocalDateTime joinDate;

    @Value("#{jobParameters[joinDate]}")
    public void setJoinDate(String joinDate){
        this.joinDate = LocalDateTime.parse(joinDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}