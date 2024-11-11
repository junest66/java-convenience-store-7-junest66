package store.entity;

import store.dto.Amount;
import store.dto.FinalPaymentProduct;

public class Payment {
    private final Cart cart;
    private final Membership membership;

    public Payment(Cart cart) {
        this.cart = cart;
        this.membership = new Membership();
    }

    public Amount process(boolean isMemberShip, FinalPaymentProduct finalPaymentProduct) {
        int totalPrice = cart.getTotalPrice();
        int promotionDiscount = finalPaymentProduct.getOfferedItemsPrice();
        int membershipDiscount = 0;
        if (isMemberShip) {
            membershipDiscount = membership.discount(finalPaymentProduct.nonDiscountedCart());
        }
        int finalPrice = totalPrice - promotionDiscount - membershipDiscount;
        return new Amount(totalPrice, promotionDiscount, membershipDiscount, finalPrice);
    }
}
