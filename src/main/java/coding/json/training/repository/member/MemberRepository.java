package coding.json.training.repository.member;

import coding.json.training.domain.Member;
import coding.json.training.dto.BestPostAdminDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    // PagingAndSortingRepository 또는 Pageable
}
