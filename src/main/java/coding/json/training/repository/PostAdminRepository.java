package coding.json.training.repository;

import coding.json.training.domain.dept.Category;
import coding.json.training.domain.dept.PostAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface PostAdminRepository extends JpaRepository<PostAdmin, Long> {


    @Query("select p.category, max(p.resolutionDegree) from PostAdmin p group by p.category")
    List<Object[]> findMaxDegreeByCategory();


}
