package coding.json.practice.batch.jobs.etc;

import coding.json.practice.batch.jobs.etc.entity.ProductStatus;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
public class CreateJobParameter{

    private LocalDate createDate;
    private ProductStatus status;

    public CreateJobParameter(String createDateStr, ProductStatus status){
        this.createDate = LocalDate.parse(createDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.status = status;
    }

}
