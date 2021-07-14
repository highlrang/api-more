package coding.json.training.service;

import coding.json.training.dto.Grade;
import coding.json.training.dto.MemberDto;
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


    public void findAllMembers(){
        memberRepository.findAll();
    }

    public List<MemberDto> findMembersByGrade(Grade grade){
        return memberRepository.findByGrade(grade)
            .stream()
            .map(m -> new MemberDto(m))
            .collect(Collectors.toList());
    }


    @Data
    @NoArgsConstructor
    static class Job{
        private String kinds;
    }

    // restTemplate 추가하기
    public void jobUpdate() {
        RestTemplate restTemplate = new RestTemplate(); // final로 바꾸기
        HttpHeaders httpHeaders = new HttpHeaders();

        // UriComponents UriComponentsBuilder
        // HttpEntity request = new HttpEntity<>(post용 data, httpHeaders);
        // restTemplate.postForObject(url, entity, request); // >> postForEntity는 restEntity로 받기 가능??
    }
}
