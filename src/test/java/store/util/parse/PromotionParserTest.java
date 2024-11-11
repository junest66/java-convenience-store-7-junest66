package store.util.parse;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import store.entity.Promotion;

class PromotionParserTest {
    private static final List<String> promotionData = List.of(
            "name,buy,get,start_date,end_date",
            "탄산2+1,2,1,2024-01-01,2024-12-31",
            "MD추천상품,1,1,2024-01-01,2024-12-31",
            "반짝할인,1,1,2024-11-01,2024-11-30"
    );

    @Test
    void 프로모션_파싱_테스트() {
        Map<String, Promotion> promotions = PromotionParser.parse(promotionData);
        Map<String, Promotion> expectedPromotions = Map.of(
                "탄산2+1", new Promotion("탄산2+1", 2, 1, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31)),
                "MD추천상품", new Promotion("MD추천상품", 1, 1, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31)),
                "반짝할인", new Promotion("반짝할인", 1, 1, LocalDate.of(2024, 11, 1), LocalDate.of(2024, 11, 30))
        );
        assertThat(promotions).containsExactlyInAnyOrderEntriesOf(expectedPromotions);
    }
}
