package store.entity;

public class Membership {
    private static final int discountPercent = 30;
    private static final int maxDiscountAmount = 8000;

    public int discount(Cart nonDiscountedCart) {
        int discountAmount = nonDiscountedCart.getTotalPrice() * discountPercent / 100;
        return Math.min(discountAmount, maxDiscountAmount);
    }
}
