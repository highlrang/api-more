package coding.json.training.repository;

import coding.json.training.domain.dept.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository<T extends Department> extends JpaRepository<T, Long> {


}
