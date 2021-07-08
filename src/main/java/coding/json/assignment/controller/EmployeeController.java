package coding.json.assignment.controller;

import coding.json.assignment.service.EmployeeService;
import coding.json.assignment.vo.EmployeeVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    // 여러 API를 비동기로 불러와서 이 json format에 데이터 합쳐서 return해야함
    @GetMapping("/api/v1/newcomers")
    public List<EmployeeVO> newcommers(){

        List<EmployeeVO> newcommers = employeeService.getNewcommers();
        return newcommers;

    }

}
