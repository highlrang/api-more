package coding.json.training.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import coding.json.training.domain.dept.PostAdmin;
public interface PostAdminRepository extends JpaRepository<PostAdmin, Long> {

}
