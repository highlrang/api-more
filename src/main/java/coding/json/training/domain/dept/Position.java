package coding.json.training.domain.dept;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Position {

    Staff("사원"), SeniorStaff("대리"), AssistantManager("주임"), Manager("대리"), DeputyGeneralManager("과장"), GeneralManager("차장");

    private final String name;
}
