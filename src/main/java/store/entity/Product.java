package store.entity;

import java.util.Objects;

public class Product {
    private final String name;
    private final int price;
    private final Promotion promotion;
    private final DateProvider dateProvider;
    private int quantity;
    private int promotionQuantity;

    public Product(String name, int price, int count, Promotion promotion, DateProvider dateProvider) {
        this.name = name;
        this.price = price;
        this.promotion = promotion;
        this.dateProvider = dateProvider;
        if (promotion == null) {
            this.quantity = count;
            return;
        }
        this.promotionQuantity = count;
    }

    public void addQuantity(int count, Promotion promotion) {
        if (promotion == null) {
            this.promotionQuantity = count;
            return;
        }
        this.quantity = count;
    }

    public void reduceQuantity(int quantity) {
        this.quantity -= quantity;
    }

    public void reducePromotionQuantity(int promotionQuantity) {
        if (this.promotionQuantity >= promotionQuantity) {
            this.promotionQuantity -= promotionQuantity;
            return;
        }
        int remainingToReduce = promotionQuantity - this.promotionQuantity;
        this.promotionQuantity = 0;
        this.quantity -= remainingToReduce;
    }

    boolean isStockAvailable(int purchaseQuantity) {
        if (isPromotionEligible()) {
            return (quantity + promotionQuantity) >= purchaseQuantity;
        }
        return quantity >= purchaseQuantity;
    }

    public boolean isPromotionEligible() {
        return promotion != null && promotion.isPromotionValid(dateProvider.now());
    }

    public boolean isInsufficientPromotion(Integer value) {
        return promotion.isInsufficientPromotion(value);
    }

    public Integer getInsufficientPromotionQuantity(Integer value) {
        return promotion.getInsufficientPromotionQuantity(value);
    }

    public boolean isInSufficientPromotionStock(Integer purchaseQuantity) {
        int maxPromotionSet = promotionQuantity / promotion.getCycle();
        int maxPromotionQuantity = maxPromotionSet * promotion.getCycle();
        return maxPromotionQuantity < purchaseQuantity;
    }

    public Integer getNonEligiblePromotionQuantity(Integer purchaseQuantity) {
        int maxPromotionSet = promotionQuantity / promotion.getCycle();
        int maxPromotionQuantity = maxPromotionSet * promotion.getCycle();
        if (purchaseQuantity <= maxPromotionQuantity) {
            return 0;
        }
        return purchaseQuantity - maxPromotionQuantity;
    }

    public Integer calculateEligiblePromotionQuantity(Integer purchaseQuantity) {
        return purchaseQuantity / promotion.getCycle();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product product)) {
            return false;
        }
        return Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPromotionQuantity() {
        return promotionQuantity;
    }
}
