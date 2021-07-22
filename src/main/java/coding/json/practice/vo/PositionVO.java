package coding.json.practice.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PositionVO {

    private Long id;
    private String department; // Department.class 생략
    private String job;

    public PositionVO(Long id, String department, String job){
        this.id = id;
        this.department = department;
        this.job = job;

    }
}
