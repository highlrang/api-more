package coding.json.training.service;

import coding.json.training.domain.Member;
import coding.json.training.domain.dept.Category;
import coding.json.training.dto.MemberRequestDto;
import coding.json.training.dto.MemberResponseDto;
import coding.json.training.dto.PostAdminDto;
import coding.json.training.repository.member.MemberQuerydslRepository;
import coding.json.training.repository.member.MemberRepository;
import coding.json.training.repository.PostAdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberQuerydslRepository memberQuerydslRepository;
    private final PostAdminRepository postAdminRepository;

    public List<MemberResponseDto> findMembers(){
        return memberRepository.findAll()
            .stream()
            .map(MemberResponseDto::new)
            .collect(Collectors.toList());
    }

    public MemberResponseDto findById(Long id){
        Member member = memberRepository.findById(id).orElseThrow(IllegalStateException::new);
        return new MemberResponseDto(member);
    }

    public Long save(MemberRequestDto memberRequestDto){
        Member member = memberRequestDto.toEntity();
        return memberRepository.save(memberRequestDto.toEntity()).getId();
    }

    public void delete(Long id){
        Member member = memberRepository.findById(id)
                .orElseThrow(IllegalStateException::new);
        memberRepository.delete(member); // cascade로 department에 member 삭제
    }

    public List<PostAdminDto> findPostAdmin() {
        List<PostAdminDto> postAdmins = new ArrayList<>();
        for(Category category:Category.values()){
            List<Member> members = memberQuerydslRepository.findPostAdmins(category);
            postAdmins.addAll(
                    members.stream()
                           .map(member -> new PostAdminDto(member, category))
                           .collect(Collectors.toList())
            );
        }
        return postAdmins;

        // 애초에 post-admin 출력 시 category도 출력되는 경우면은
        // 출력 후 key value로 분류하는 것도 괜찮
        /*
        for(Entry<Category, Integer> entry : maxDegree.entrySet()){
            entry.getKey();
            entry.getValue();
        }
         */

    }
}
