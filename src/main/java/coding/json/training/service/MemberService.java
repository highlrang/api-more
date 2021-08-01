package coding.json.training.service;

import coding.json.training.domain.Member;
import coding.json.training.domain.dept.Category;
import coding.json.training.dto.MemberRequestDto;
import coding.json.training.dto.MemberResponseDto;
import coding.json.training.repository.member.MemberQuerydslRepository;
import coding.json.training.repository.member.MemberRepository;
import coding.json.training.repository.PostAdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    public Long saveMember(MemberRequestDto memberRequestDto){
        Member member = memberRequestDto.toEntity();
        return memberRepository.save(memberRequestDto.toEntity()).getId();
    }

    public void deleteMember(Long id){
        Member member = memberRepository.findById(id)
                .orElseThrow(IllegalStateException::new);

        member.deleteDepartment();
        memberRepository.delete(member);
    }

    public Map<Category, List<Member>> findPostAdmin() { // not PostAdminDto
        Map<Category, List<Member>> matching = new HashMap<>();
        for(Category category:Category.values()){
            List<Member> members = memberQuerydslRepository.findPostAdmins(category);
            matching.put(category, members);
        }
        return matching;
        /*
        for(Entry<Category, Integer> entry : maxDegree.entrySet()){
            entry.getKey();
            entry.getValue();
        }
         */

    }
}
