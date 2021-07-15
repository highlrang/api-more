package coding.json.training.domain.dept;

import coding.json.training.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;

@Entity
@DiscriminatorValue("post_admin")
@NoArgsConstructor
@Getter
public class PostAdmin extends Department{ // HumanResource, Design, Marketing, Developer, CustomerService extends Department

    private Category category;
    private Integer resolutionDegree; // default zero

    @Builder
    public PostAdmin(Position position, Category category){
        this.addPosition(position);
        this.category = category;
    }


}
