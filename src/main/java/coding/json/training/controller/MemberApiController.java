package coding.json.training.controller;

import coding.json.training.dto.BestPostAdminDto;
import coding.json.training.dto.MemberResponseDto;
import coding.json.training.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor // 만약 안되면 @Autowired로 해보기
public class MemberApiController {

    private final MemberService memberService;

    @GetMapping("/external/api/v1/members/post-admin/best")
    public List<BestPostAdminDto> members(){

        return memberService.findBestPostAdmin();

    }




}
