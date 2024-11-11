package store.service;

import java.util.List;
import java.util.Map;
import store.dto.Amount;
import store.dto.ProductItem;
import store.dto.Receipt;
import store.entity.Cart;
import store.dto.FinalPaymentProduct;
import store.entity.Payment;
import store.entity.Product;
import store.entity.Stock;

public class CartService {
    private final Cart cart;
    private final Stock stock;

    public CartService(Stock stock) {
        this.cart = new Cart();
        this.stock = stock;
    }

    public void purchase(Map<String, Integer> items) {
        stock.validateAll(items);
        items.forEach((productName, quantity) -> {
            Product product = stock.findByName(productName);
            cart.addProduct(product, quantity);
        });
    }

    public List<ProductItem> getPromotionAdditionalItems() {
        return cart.getInsufficientPromotionItems();
    }

    public void addPromotionItems(List<ProductItem> items) {
        for (ProductItem item : items) {
            Product product = stock.findByName(item.productName());
            cart.addPromotionItems(product, item.quantity());
        }
    }

    public List<ProductItem> getNonPromotion() {
        return cart.getNonPromotionItems();
    }

    public void removeNonPromotionItems(List<ProductItem> removeNonPromotionItems) {
        for (ProductItem item : removeNonPromotionItems) {
            Product product = stock.findByName(item.productName());
            cart.removeNonPromotionItems(product, item.quantity());
        }
    }

    public Receipt pay(boolean isMembership) {
        Payment payment = new Payment(cart);
        FinalPaymentProduct finalPaymentProduct = cart.getFinalPaymentItems();
        stock.update(cart.getCartItem());
        Amount amount = payment.process(isMembership, finalPaymentProduct);
        return new Receipt(cart.getCartItemsWithPrice(), finalPaymentProduct.getOfferedItemsDto(), amount);
    }
}
