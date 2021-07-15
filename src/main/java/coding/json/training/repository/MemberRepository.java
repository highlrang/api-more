package coding.json.training.repository;

import coding.json.training.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    // 기본 제공


}
