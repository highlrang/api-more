package coding.json.training.dto;


import coding.json.training.domain.dept.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PostAdminDto {

    private Long id;
    private String name;
    private Category category;

    @Builder
    public PostAdminDto(Long id, String name, Category category) {
        this.id = id;
        this.name = name;
        this.category = category;
    }
}
