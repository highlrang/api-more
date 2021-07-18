package coding.json.training.repository.member;

import coding.json.training.dto.BestPostAdminDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class MemberQueryRepository {

    private final EntityManager em;

    // entity orm 쓸 수 없어서(추상부모의 자식이라 getter로 안 불러짐) >> native query
    // 그냥 query여야 dto 생성자로 entity orm으로 받을 수 있음
    public List<BestPostAdminDto> findBestPostAdmins(String category, Integer degree){
        return em.createNativeQuery("select m.id as id, m.name as name, category, d.resolution_degree as resolutionDegree" +
                " from member m join department d on m.department_id = d.id" +
                " where d.category =:category and d.resolution_degree =:degree",
                "BestPostAdminMapping")
                .setParameter("category", category)
                .setParameter("degree", degree)
                .getResultList();

    }
}
