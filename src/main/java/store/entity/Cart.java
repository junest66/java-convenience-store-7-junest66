package store.entity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import store.dto.FinalPaymentProduct;
import store.dto.ProductItem;
import store.dto.ProductItemWithPrice;

public class Cart {
    private final Map<Product, Integer> cartItem;

    public Cart() {
        this.cartItem = new LinkedHashMap<>();
    }

    public void addProduct(Product product, int quantity) {
        cartItem.put(product, cartItem.getOrDefault(product, 0) + quantity);
    }

    public void addPromotionItems(Product product, int additionalQuantity) {
        cartItem.put(product, cartItem.get(product) + additionalQuantity);
    }

    public void removeNonPromotionItems(Product product, int quantity) {
        cartItem.put(product, cartItem.get(product) - quantity);
    }

    public List<ProductItem> getInsufficientPromotionItems() {
        List<ProductItem> insufficientPromotionItems = new ArrayList<>();
        for (Map.Entry<Product, Integer> cart : getPromotionItems().entrySet()) {
            Product product = cart.getKey();
            if (product.isInsufficientPromotion(cart.getValue())) {
                int insufficientQuantity = product.getInsufficientPromotionQuantity(cart.getValue());
                insufficientPromotionItems.add(new ProductItem(product.getName(), insufficientQuantity));
            }
        }
        return insufficientPromotionItems;
    }

    public List<ProductItem> getNonPromotionItems() {
        List<ProductItem> nonPromotionItems = new ArrayList<>();
        for (Map.Entry<Product, Integer> cart : getPromotionItems().entrySet()) {
            Product product = cart.getKey();
            if (product.isInSufficientPromotionStock(cart.getValue())) {
                int nonPromotionQuantity = product.getNonEligiblePromotionQuantity(cart.getValue());
                nonPromotionItems.add(new ProductItem(product.getName(), nonPromotionQuantity));
            }
        }
        return nonPromotionItems;
    }

    public Map<Product, Integer> getPromotionItems() {
        Map<Product, Integer> promotionItems = new LinkedHashMap<>();
        for (Map.Entry<Product, Integer> entry : cartItem.entrySet()) {
            Product product = entry.getKey();
            if (product.isPromotionEligible()) {
                promotionItems.put(product, entry.getValue());
            }
        }
        return promotionItems;
    }

    public FinalPaymentProduct getFinalPaymentItems() {
        Cart discountedCart = new Cart();
        Cart nonDiscountedCart = new Cart();
        for (Map.Entry<Product, Integer> entry : cartItem.entrySet()) {
            if (isEligibleForPromotion(entry.getKey())) {
                processPromotionProduct(entry.getKey(), entry.getValue(), discountedCart, nonDiscountedCart);
            } else {
                nonDiscountedCart.addProduct(entry.getKey(), entry.getValue());
            }
        }
        return new FinalPaymentProduct(discountedCart, nonDiscountedCart);
    }

    public int getTotalPrice() {
        return cartItem.entrySet().stream()
                .mapToInt(entry -> entry.getKey().getPrice() * entry.getValue())
                .sum();
    }

    public List<ProductItemWithPrice> getCartItemsWithPrice() {
        return cartItem.entrySet().stream()
                .map(entry -> new ProductItemWithPrice(entry.getKey().getName(), entry.getValue(),
                        entry.getKey().getPrice()))
                .toList();
    }

    public List<ProductItem> getCartItems() {
        return cartItem.entrySet().stream()
                .map(entry -> new ProductItem(entry.getKey().getName(), entry.getValue()))
                .toList();
    }

    private boolean isEligibleForPromotion(Product product) {
        return product.getPromotion() != null && product.isPromotionEligible();
    }

    private void processPromotionProduct(Product product, Integer orderedQuantity, Cart discountedCart,
                                         Cart nonDiscountedCart) {
        int nonPromotionQuantity = product.getNonEligiblePromotionQuantity(orderedQuantity);
        int promotionCount = orderedQuantity - nonPromotionQuantity;
        int promotableQuantity = product.calculateEligiblePromotionQuantity(promotionCount);
        if (promotableQuantity > 0) {
            discountedCart.addProduct(product, promotableQuantity);
        }
        if (nonPromotionQuantity > 0) {
            nonDiscountedCart.addProduct(product, nonPromotionQuantity);
        }
    }

    public Map<Product, Integer> getCartItem() {
        return cartItem;
    }
}
