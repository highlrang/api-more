package coding.json.training.service;

import coding.json.training.domain.Member;
import coding.json.training.dto.MemberRequestDto;
import coding.json.training.dto.MemberResponseDto;
import coding.json.training.repository.MemberRepository;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

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

    // restTemplate !!!
}
