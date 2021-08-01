package coding.json.training.repository;

import coding.json.training.domain.dept.Category;
import coding.json.training.domain.dept.Position;
import coding.json.training.domain.dept.PostAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PostAdminRepository extends JpaRepository<PostAdmin, Long> {

    Optional<PostAdmin> findByCategoryAndPosition(Category category, Position position);

}
