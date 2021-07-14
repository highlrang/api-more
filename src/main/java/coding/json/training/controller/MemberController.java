package coding.json.training.controller;

import coding.json.training.dto.Grade;
import coding.json.training.dto.MemberDto;
import coding.json.training.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor // 만약 안되면 @Autowired로 해보기
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/api/v1/members")
    public List<MemberDto> members(){

        return memberService.findMembersByGrade(Grade.VIP);

    }




}
