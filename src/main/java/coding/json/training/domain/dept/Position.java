package coding.json.training.domain.dept;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Position{

    Staff("사원"), SeniorStaff("주임"), AssistantManager("대리"),
    Manager("과장"), DeputyGeneralManager("차장"), GeneralManager("부장");

    private final String name;

    public String getKey(){
        return name();
    }

    public String getValue(){
        return name;
    }
}
