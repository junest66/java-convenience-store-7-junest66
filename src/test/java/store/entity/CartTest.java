package store.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.dto.FinalPaymentProduct;
import store.dto.ProductItem;

class CartTest {

    private Cart cart;
    private Product productWithPromotion;
    private Product productWithoutPromotion;
    private Product productWithOneToOnePromotion;
    private DateProvider dateProvider = new TestDateProvider(LocalDate.of(2024, 1, 1));
    private Promotion promotion = new Promotion("탄산2+1", 2, 1, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));
    private Promotion oneToOnePromotion = new Promotion("1+1", 1, 1, LocalDate.of(2024, 1, 1),
            LocalDate.of(2024, 12, 31));

    @BeforeEach
    void setUp() {
        cart = new Cart();
        productWithPromotion = new Product("콜라", 1000, 10, promotion, dateProvider);
        productWithPromotion.addQuantity(10, promotion);
        productWithoutPromotion = new Product("사이다", 1500, 5, null, dateProvider);
        productWithOneToOnePromotion = new Product("오렌지주스", 1500, 9, oneToOnePromotion, dateProvider);
    }

    @Test
    void 프로모션_제품중_부족하게_가져온거_테스트() {
        cart.addProduct(productWithOneToOnePromotion, 1);
        List<ProductItem> insufficientPromotionItems = cart.getInsufficientPromotionItems();
        assertThat(insufficientPromotionItems).containsExactly(new ProductItem("오렌지주스", 1));
    }

    @Test
    void 프로모션_적용되지만_프로모션_재고가_부족한_상품_확인_테스트() {
        cart.addProduct(productWithPromotion, 13);
        List<ProductItem> nonPromotionItems = cart.getNonPromotionItems();
        assertThat(nonPromotionItems).containsExactly(new ProductItem("콜라", 4));
    }

    @Test
    void 프로모션_적용_상품_확인_테스트() {
        cart.addProduct(productWithPromotion, 6);
        Map<Product, Integer> items = cart.getPromotionItems();
        assertThat(items).containsEntry(productWithPromotion, 6);
    }

    @Test
    void 프로모션_적용_부족_상품_확인_테스트() {
        cart.addProduct(productWithPromotion, 11);
        List<ProductItem> insufficientPromotionItems = cart.getInsufficientPromotionItems();
        assertThat(insufficientPromotionItems.get(0)).isEqualTo(new ProductItem("콜라", 1));
    }

    @Test
    void 프로모션_항목_확인_테스트() {
        cart.addProduct(productWithPromotion, 3);
        cart.addProduct(productWithOneToOnePromotion, 5);
        cart.addProduct(productWithoutPromotion, 1);
        Map<Product, Integer> promotionItems = cart.getPromotionItems();
        assertThat(promotionItems).containsEntry(productWithPromotion, 3);
        assertThat(promotionItems).containsEntry(productWithOneToOnePromotion, 5);
    }

    @Test
    void 최종_결제_항목_생성_테스트() {
        cart.addProduct(productWithPromotion, 13);
        cart.addProduct(productWithoutPromotion, 4);

        FinalPaymentProduct finalPayment = cart.getFinalPaymentItems();
        assertThat(finalPayment.discountedCart().getCartItem()).containsEntry(productWithPromotion, 3);
        assertThat(finalPayment.nonDiscountedCart().getCartItem()).containsEntry(productWithoutPromotion, 4);
        assertThat(finalPayment.nonDiscountedCart().getCartItem()).containsEntry(productWithPromotion, 4);
    }

    @Test
    void 총_가격_계산_테스트() {
        cart.addProduct(productWithPromotion, 2);
        cart.addProduct(productWithoutPromotion, 3);
        int totalPrice = cart.getTotalPrice();
        assertThat(totalPrice).isEqualTo(6500);
    }
}
