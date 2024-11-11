package store.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.dto.Amount;
import store.dto.FinalPaymentProduct;

class PaymentTest {
    private Cart cart;
    private Product productWithPromotion;
    private Product productWithoutPromotion;
    private DateProvider dateProvider;
    private Promotion promotion;
    private Promotion oneToOnePromotion;

    @BeforeEach
    void setUp() {
        dateProvider = new TestDateProvider(LocalDate.of(2024, 1, 1));
        promotion = new Promotion("탄산2+1", 2, 1, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));
        oneToOnePromotion = new Promotion("MD추천상품", 1, 1, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));
        cart = new Cart();
    }

    @Test
    void 멤버십을_적용해_최종결제를_진행해_금액을_확인한다() {
        setupCockeWithTen();
        Amount expectedAmount = new Amount(13000, 1000, 3000, 9000);
        verifyPaymentAmount(true, expectedAmount);
    }

    @Test
    void 멤버십을_적용하지않고_최종결제를_진행해_금액을_확인한다() {
        setupCockeWithTen();
        Amount expectedAmount = new Amount(13000, 1000, 0, 12000);
        verifyPaymentAmount(false, expectedAmount);
    }

    @Test
    void 프로모션_재고부족_멤버십미적용_추가_정가결제할때_테스트() {
        setupCockeWithSeven();
        Amount expectedAmount = new Amount(10000, 2000, 0, 8000);
        verifyPaymentAmount(false, expectedAmount);
    }

    @Test
    void 멤버십할인적용시_오렌지_1개_구매_테스트() {
        setUpOrange();
        Amount expectedAmount = new Amount(1800, 0, 0, 1800);
        verifyPaymentAmount(true, expectedAmount);
    }

    private void setupCockeWithSeven() {
        cart = new Cart();
        productWithPromotion = new Product("콜라", 1000, 7, promotion, dateProvider);
        productWithPromotion.addQuantity(10, promotion);
        cart.addProduct(productWithPromotion, 10);
    }

    private void setupCockeWithTen() {
        cart = new Cart();
        productWithPromotion = new Product("콜라", 1000, 10, promotion, dateProvider);
        productWithPromotion.addQuantity(10, promotion);
        productWithoutPromotion = new Product("에너지바", 2000, 5, null, dateProvider);
        cart.addProduct(productWithPromotion, 3);
        cart.addProduct(productWithoutPromotion, 5);
    }

    private void setUpOrange() {
        cart = new Cart();
        productWithPromotion = new Product("오렌지", 1800, 9, oneToOnePromotion, dateProvider);
        cart.addProduct(productWithPromotion, 1);
    }

    private void verifyPaymentAmount(boolean isMembershipApplied, Amount expectedAmount) {
        FinalPaymentProduct finalPaymentItems = cart.getFinalPaymentItems();
        Payment payment = new Payment(cart);
        Amount actualAmount = payment.process(isMembershipApplied, finalPaymentItems);
        assertThat(actualAmount).usingRecursiveComparison().isEqualTo(expectedAmount);
    }
}
