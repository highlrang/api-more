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

    @Value("#{jobParameters[email]}")
    private String email;

    /*
    @Value("#{jobParameters[email]}")
    public void setEmail(String email){ // 형변환 시 세터 사용
        this.email = email;
        // this.txDate = LocalDate.parse(txDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

     */
}