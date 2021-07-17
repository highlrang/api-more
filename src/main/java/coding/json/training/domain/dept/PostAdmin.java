package coding.json.training.domain.dept;

import coding.json.training.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;

@Entity
@DiscriminatorValue("post_admin")
@NoArgsConstructor
@DynamicInsert
@Getter
public class PostAdmin extends Department{ // HumanResource, Design, Marketing, Developer, CustomerService extends Department

    @Enumerated(EnumType.STRING)
    private Category category;
    @Column(columnDefinition = "integer default 0")
    private Integer resolutionDegree;

    @Builder
    public PostAdmin(Position position, Category category){
        this.addPosition(position);
        this.category = category;
    }

    public void updateResolutionDegree(int score){
        resolutionDegree += score;
    }

}
