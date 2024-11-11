package store.dto;

import store.entity.Product;

public record StockResponse(String name, int price, String quantity, String promotionQuantity, String promotionName) {
    private static final String NONE_STOCK = "재고 없음";
    private static final String QUANTITY_UNIT = "개";

    public static StockResponse from(Product product) {
        String promotionName = "";
        if (product.getPromotion() != null) {
            promotionName = product.getPromotion().getName();
        }
        return new StockResponse(product.getName(), product.getPrice(), formatQuantity(product.getQuantity()),
                formatQuantity(product.getPromotionQuantity()), promotionName);
    }

    private static String formatQuantity(int quantity) {
        if (quantity == 0) {
            return NONE_STOCK;
        }
        return quantity + QUANTITY_UNIT;
    }
}
