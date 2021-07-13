package coding.json.training.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Interests {

    MUSIC("음악"), MOVIE("영화"), DRAWING("그림"), EXERCISE("운동");

    private final String kinds;
}
