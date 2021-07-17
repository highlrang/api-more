package coding.json.training.controller;

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
public class MemberController {

    private final MemberService memberService;

    /*
    external 훌륭한 직원들 비동기로 뽑아오고
    internal에서 resttemplate으로 다루기

    internal에서 해결 시급한 카테고리 게시글 뽑아서

    internal에서 두 종류의 데이터 더미를 새로운 DTO 생성 후 매칭
     */

    @GetMapping("/external/api/v1/members/post-admin/best")
    public List<MemberResponseDto> members(){

        return memberService.findBestPostAdmin();

    }




}
