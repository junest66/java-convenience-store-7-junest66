package store.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static store.constant.Constant.ERROR_PREFIX;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class StockTest {
    private static final String NON_EXIST_PRODUCT_MESSAGE = ERROR_PREFIX + "존재하지 않는 상품입니다. 다시 입력해 주세요.";
    private static final String STOCK_EXCEED_MESSAGE = ERROR_PREFIX + "재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.";

    private static Stock stock;
    private static Promotion promotion = new Promotion("탄산2+1", 2, 1, LocalDate.of(2024, 1, 1),
            LocalDate.of(2024, 12, 31));
    private static DateProvider dateProvider = new TestDateProvider(LocalDate.of(2024, 1, 1));
    private static Product coke = new Product("콜라", 1000, 10, promotion, dateProvider);
    private static Product sada = new Product("사이다", 1200, 5, promotion, dateProvider);
    private static Product general = new Product("일반제품", 1200, 5, null, dateProvider);

    @BeforeAll
    static void beforeAll() {
        coke.addQuantity(5, promotion);
        sada.addQuantity(5, promotion);
        stock = new Stock(List.of(coke, sada, general));
    }

    @Test
    void 제품의_이름으로_제품을_찾는다() {
        Product product = stock.findByName("콜라");
        assertThat(product.getName()).isEqualTo("콜라");
    }

    @Test
    void 재고의_없는_제품이름을_찾을때_예외_발생() {
        assertThatThrownBy(() -> stock.findByName("콜라1"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 재고를_업데이트한다() {
        Map<Product, Integer> cartItems = Map.of(coke, 5, sada, 7, general, 2);
        stock.update(cartItems);
        assertThat(coke.getPromotionQuantity()).isEqualTo(5);
        assertThat(coke.getQuantity()).isEqualTo(5);
        assertThat(sada.getPromotionQuantity()).isEqualTo(0);
        assertThat(sada.getQuantity()).isEqualTo(3);
        assertThat(general.getQuantity()).isEqualTo(3);
    }

    @Test
    void 구매하는_제품이_존재하지_않을때() {
        assertThatThrownBy(() -> stock.validateAll(Map.of("콜1", 5)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(NON_EXIST_PRODUCT_MESSAGE);
    }

    @Test
    void 프로모션을_적용할때_재고가_부족할때() {
        assertThatThrownBy(() -> stock.validateAll(Map.of("콜라", 16)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(STOCK_EXCEED_MESSAGE);
    }

    @Test
    void 구매하는_제품이_존재하면서_재고가_부족하지_않을때() {
        assertDoesNotThrow(() -> stock.validateAll(Map.of("콜라", 5)));
    }
}
