package store.constant;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class YesOrNoTest {
    @ParameterizedTest
    @ValueSource(strings = {"Y", "N"})
    void Enum_생성_테스트(String value) {
        assertDoesNotThrow(() -> YesOrNo.of(value));
    }

    @ParameterizedTest
    @ValueSource(strings = {"y", "n", "yes", "no", "YES", "NO"})
    void Enum_생성_예외_테스트(String value) {
        assertThatThrownBy(() -> YesOrNo.of(value))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
