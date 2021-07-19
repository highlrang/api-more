package coding.json.training.dto;


import coding.json.training.domain.dept.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class BestPostAdminDto {

    private Long id;
    private String name;
    private Category category;
    private Integer resolutionDegree;

    @Builder
    public BestPostAdminDto(Long id, String name, Category category, Integer resolutionDegree){
        this.id = id;
        this.name = name;
        this.category = category;
        this.resolutionDegree = resolutionDegree;
    }
}
