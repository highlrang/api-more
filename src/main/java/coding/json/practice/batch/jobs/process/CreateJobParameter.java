package coding.json.practice.batch.jobs.process;

import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
public class CreateJobParameter{

    private LocalDate createDate;
    private String status;

    public CreateJobParameter(String createDateStr, String status){
        this.createDate = LocalDate.parse(createDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.status = status;
    }

}
