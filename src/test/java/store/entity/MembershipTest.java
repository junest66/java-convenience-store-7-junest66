package store.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MembershipTest {
    @Test
    void 멤버십_할인_테스트_최대값을_넘기지않을때() {
        Cart nonDiscountedCart = new Cart();
        nonDiscountedCart.addProduct(new Product("콜라", 1000, 10, null, null), 5);
        nonDiscountedCart.addProduct(new Product("사이다", 1200, 5, null, null), 5);
        Membership membership = new Membership();
        int discountAmount = membership.discount(nonDiscountedCart);
        assertThat(discountAmount).isEqualTo(3300);
    }

    @Test
    void 멤버십_할인_테스트_최대값을_넘길때() {
        Cart nonDiscountedCart = new Cart();
        nonDiscountedCart.addProduct(new Product("콜라", 1000, 10, null, null), 100);
        nonDiscountedCart.addProduct(new Product("사이다", 1200, 5, null, null), 100);
        Membership membership = new Membership();
        int discountAmount = membership.discount(nonDiscountedCart);
        assertThat(discountAmount).isEqualTo(8000);
    }

}
