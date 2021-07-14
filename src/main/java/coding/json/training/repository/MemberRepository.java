package coding.json.training.repository;

import coding.json.training.domain.Member;
import coding.json.training.dto.Grade;
import coding.json.training.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    // 기본 제공
    List<Member> findByGrade(Grade grade);

}
