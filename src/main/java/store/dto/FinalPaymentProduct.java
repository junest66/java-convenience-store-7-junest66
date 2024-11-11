package store.dto;

import java.util.List;
import store.entity.Cart;

public record FinalPaymentProduct(Cart discountedCart, Cart nonDiscountedCart) {
    public List<ProductItem> getOfferedItemsDto() {
        return discountedCart.getCartItems();
    }

    public int getOfferedItemsPrice() {
        return discountedCart.getTotalPrice();
    }
}
