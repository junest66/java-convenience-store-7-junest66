package store.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PromotionTest {
    private static Promotion oneToOnePromotion;
    private static Promotion twoToOnePromotion;
    private static Promotion twoToTwoPromotion;

    @BeforeAll
    static void setUp() {
        oneToOnePromotion = new Promotion("1+1", 1, 1,
                LocalDate.of(2024, 11, 10),
                LocalDate.of(2024, 12, 10));
        twoToOnePromotion = new Promotion("2+1", 2, 1, null, null);
        twoToTwoPromotion = new Promotion("2+2", 2, 2, null, null);
    }

    @ParameterizedTest(name = "날짜 {0}에 대한 프로모션 유효성은 {1}이어야 한다")
    @CsvSource({
            "2024-11-09, false",
            "2024-11-11, true",
            "2024-11-10, true",
            "2024-12-10, true",
            "2024-12-11, false"
    })
    void 프로모션_유효성_테스트(String date, boolean expected) {
        LocalDate testDate = LocalDate.parse(date);
        assertThat(oneToOnePromotion.isPromotionValid(testDate)).isEqualTo(expected);
    }

    @ParameterizedTest(name = "1+1 프로모션 - 수량 {0} -> 부족 여부: {1}")
    @CsvSource({"1, true", "2, false", "3, true", "4, false"})
    void 원플러스원_부족_여부_테스트(int quantity, boolean expected) {
        assertThat(oneToOnePromotion.isInsufficientPromotion(quantity)).isEqualTo(expected);
    }

    @ParameterizedTest(name = "2+1 프로모션 - 수량 {0} -> 부족 여부: {1}")
    @CsvSource({"1, false", "2, true", "3, false", "4, false", "5, true", "6, false"})
    void 투플러스원_부족_여부_테스트(int quantity, boolean expected) {
        assertThat(twoToOnePromotion.isInsufficientPromotion(quantity)).isEqualTo(expected);
    }

    @ParameterizedTest(name = "2+2 프로모션 - 수량 {0} -> 부족 여부: {1}")
    @CsvSource({"1, false", "2, true", "3, true", "4, false", "5, false", "6, true", "7, true", "8, false"})
    void 투플러스투_부족_여부_테스트(int quantity, boolean expected) {
        assertThat(twoToTwoPromotion.isInsufficientPromotion(quantity)).isEqualTo(expected);
    }

    @ParameterizedTest(name = "1+1 프로모션 - 수량 {0} -> 부족한 수량: {1}")
    @CsvSource({"1, 1", "3, 1"})
    void 원플러스원_부족_수량_테스트(int quantity, int expectedInsufficient) {
        assertThat(oneToOnePromotion.getInsufficientPromotionQuantity(quantity)).isEqualTo(expectedInsufficient);
    }

    @ParameterizedTest(name = "2+1 프로모션 - 수량 {0} -> 부족한 수량: {1}")
    @CsvSource({"2, 1", "5, 1"})
    void 투플러스원_부족_수량_테스트(int quantity, int expectedInsufficient) {
        assertThat(twoToOnePromotion.getInsufficientPromotionQuantity(quantity)).isEqualTo(expectedInsufficient);
    }

    @ParameterizedTest(name = "2+2 프로모션 - 수량 {0} -> 부족한 수량: {1}")
    @CsvSource({"2, 2", "3, 1", "6, 2", "7, 1"})
    void 투플러스투_부족_수량_테스트(int quantity, int expectedInsufficient) {
        assertThat(twoToTwoPromotion.getInsufficientPromotionQuantity(quantity)).isEqualTo(expectedInsufficient);
    }
}
