package store.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ProductTest {
    private DateProvider dateProvider = new TestDateProvider(LocalDate.of(2024, 1, 1));
    private DateProvider prePromotionDateProvider = new TestDateProvider(LocalDate.of(2025, 1, 1));
    private Promotion promotion = new Promotion("탄산2+1", 2, 1, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));
    private Product product = new Product("콜라", 1000, 10, promotion, dateProvider);
    private Product generalProduct = new Product("콜라", 1000, 10, null, dateProvider);

    @Test
    void 프로모션상품_수량_업데이트_추가() {
        assertThat(product.getPromotionQuantity()).isEqualTo(10);
        product.addQuantity(5, promotion);
        assertThat(product.getQuantity()).isEqualTo(5);
    }

    @Test
    void 일반상품_프로모션_업데이트추가() {
        assertThat(generalProduct.getQuantity()).isEqualTo(10);
        assertThat(generalProduct.getPromotionQuantity()).isEqualTo(0);
        generalProduct.addQuantity(5, promotion);
        assertThat(generalProduct.getPromotionQuantity()).isEqualTo(5);
    }

    @Test
    void 일반수량_감소() {
        generalProduct.reduceQuantity(5);
        assertThat(generalProduct.getQuantity()).isEqualTo(5);
    }

    @ParameterizedTest
    @CsvSource({"5, 5", "10, 0"})
    void 프로모션수량_감소하는데_프로모션수량이_부족하지않을때(int promotionQuantity, int expectedQuantity) {
        product.reducePromotionQuantity(promotionQuantity);
        assertThat(product.getPromotionQuantity()).isEqualTo(expectedQuantity);
    }

    @Test
    void 프로모션수량_감소하는데_프로모션수량이_부족할때() {
        Product product = new Product("콜라", 1000, 5, promotion, null);
        product.addQuantity(5, promotion);
        product.reducePromotionQuantity(10);
        assertThat(product.getPromotionQuantity()).isEqualTo(0);
        assertThat(product.getQuantity()).isEqualTo(0);
    }

    @ParameterizedTest
    @CsvSource({"5, true", "15, true", "16, false", "20, false"})
    void 프로모션_적용_가능할때_재고_구매가능여부_테스트(int purchaseQuantity, boolean expectedResult) {
        product.addQuantity(5, promotion);
        assertThat(product.isStockAvailable(purchaseQuantity)).isEqualTo(expectedResult);
    }

    @ParameterizedTest
    @CsvSource({"4, true", "5, true", "6, false", "15, false"})
    void 프로모션_제품인데_기간이_아닐때_재고_구매가능여부_테스트(int purchaseQuantity, boolean expectedResult) {
        Product product = new Product("콜라", 1000, 10, promotion, prePromotionDateProvider);
        product.addQuantity(5, promotion);
        assertThat(product.isStockAvailable(purchaseQuantity)).isEqualTo(expectedResult);
    }

    @ParameterizedTest
    @CsvSource({"9, true", "10, true", "11, false", "15, false"})
    void 프로모션_제품이_아닐때_재고_구매가능여부_테스트(int purchaseQuantity, boolean expectedResult) {
        Product product = new Product("콜라", 1000, 10, null, prePromotionDateProvider);
        assertThat(product.isStockAvailable(purchaseQuantity)).isEqualTo(expectedResult);
    }

    @Test
    void 프로모션_적용_여부테스트() {
        assertThat(product.isPromotionEligible()).isTrue();
    }

    @Test
    void 프로모션_제품인데_적용_기간이_아닐때() {
        Product product = new Product("콜라", 1000, 10, promotion, prePromotionDateProvider);
        assertThat(product.isPromotionEligible()).isFalse();
    }

    @Test
    void 프로모션제품이_아닐때_프로모션_적용여부() {
        Product product = new Product("콜라", 1000, 10, null, dateProvider);
        assertThat(product.isPromotionEligible()).isFalse();
    }

    @ParameterizedTest
    @CsvSource({"10, true", "6, false", "8, true", "15, true"})
    void 구매하려는_제품이_프로모션제품인데_프로모션_재고가_부족할때(int purchaseQuantity, boolean expectedResult) {
        Product product = new Product("콜라", 1000, 7, promotion, dateProvider);
        assertThat(product.isInSufficientPromotionStock(purchaseQuantity)).isEqualTo(expectedResult);
    }

    @ParameterizedTest
    @CsvSource({"10, 4", "9, 3", "8, 2", "7, 1", "6, 0", "5, 0", "4, 0"})
    void 구매하려는_프로모션_제품중_몇개가_적용이_안되는지_계산(int purchaseQuantity, int expectedResult) {
        Product product = new Product("콜라", 1000, 7, promotion, dateProvider);
        assertThat(product.getNonEligiblePromotionQuantity(purchaseQuantity)).isEqualTo(expectedResult);
    }
}
