package store.util.parse;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.Test;

class ParsePurchaseItemTest {
    @Test
    void 주어진_구매상품_입력에_맞게_맵으로_잘_파싱한다() {
        String items = "[콜라-10],[사이다-3]";
        Map<String, Integer> parseItems = ParsePurchaseItem.parse(items);
        assertThat(parseItems).containsExactlyInAnyOrderEntriesOf(Map.of("콜라", 10, "사이다", 3));
    }
}
