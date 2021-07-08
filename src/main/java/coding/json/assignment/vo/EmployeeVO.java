package coding.json.assignment.vo;

import lombok.Getter;

@Getter
public class EmployeeVO {

    private Long id; // Integer?
    private String phone_number;
    private String slack_id;
    private String email;
    private String team;
    private String join_time; // 날짜 포맷 YYYY-MM-DD
    // private funnel_type Enum 타입?
    private Boolean is_junior;
    private String resume_url;
    private String[] project_names; // ArrayList로?

}
