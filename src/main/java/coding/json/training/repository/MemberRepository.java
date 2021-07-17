package coding.json.training.repository;

import coding.json.training.domain.Member;
import coding.json.training.dto.BestPostAdminDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    // PagingAndSortingRepository 또는 Pageable

    @Query(value = "select new coding.json.training.dto.BestPostAdminDto(m.id, m.name, d.category, d.resolution_degree as resolutionDegree)" +
            " from member m join department d on m.department_id = d.id" +
            " where d.category =:category and d.resolution_degree =:degree", nativeQuery = true)
    List<BestPostAdminDto> findBestPostAdmins(@Param(value="category") String category, @Param(value="degree") Integer degree);

}
