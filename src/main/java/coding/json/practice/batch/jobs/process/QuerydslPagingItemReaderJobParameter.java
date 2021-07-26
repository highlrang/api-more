package coding.json.practice.batch.jobs.process;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Slf4j
@NoArgsConstructor
public class QuerydslPagingItemReaderJobParameter {
    private String email;

    @Value("#{jobParameters[email]}")
    public void setEmail(String email) {
        // this.txDate = LocalDate.parse(txDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.email = email;
    }
}