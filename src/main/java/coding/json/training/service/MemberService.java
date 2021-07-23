package coding.json.training.service;

import coding.json.training.domain.Member;
import coding.json.training.domain.dept.Category;
import coding.json.training.dto.BestPostAdminDto;
import coding.json.training.dto.MemberRequestDto;
import coding.json.training.dto.MemberResponseDto;
import coding.json.training.repository.member.MemberQueryRepository;
import coding.json.training.repository.member.MemberQuerydslRepository;
import coding.json.training.repository.member.MemberRepository;
import coding.json.training.repository.PostAdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        return memberRepository.save(memberRequestDto.toEntity()).getId();
    }

    public void deleteMember(Long id){
        Member member = memberRepository.findById(id)
                .orElseThrow(IllegalStateException::new);

        member.getDepartment().removeMember(member);
        memberRepository.delete(member);
    }

    public List<BestPostAdminDto> findBestPostAdmin() {
        Map<Category, Integer> maxDegree = postAdminRepository.findMaxDegreeByCategory()
                .stream()
                .collect(Collectors.toMap(i -> (Category)i[0], i -> (Integer)i[1]));


        List<BestPostAdminDto> results = new ArrayList<>();
        for(Category category: maxDegree.keySet()){
            results.addAll(memberQuerydslRepository.findBestPostAdmins(category, maxDegree.get(category)));
        }

        /*
        for(Entry<Category, Integer> entry : maxDegree.entrySet()){
            entry.getKey();
            entry.getValue();
        }
         */

        return results;
    }
}
