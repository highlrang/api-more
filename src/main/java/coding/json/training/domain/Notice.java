package coding.json.training.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GeneratorType;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;

    @OneToOne(fetch = FetchType.LAZY) // cascade
    @JoinColumn(name = "member_id")
    private Member member;

    @CreatedDate
    private LocalDateTime dateTime;

    @Builder
    public Notice(String title, String content, Member member){
        this.title = title;
        this.content = content;
        this.member = member;
    }
}
