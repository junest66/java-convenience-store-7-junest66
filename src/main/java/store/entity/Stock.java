package store.entity;

import static store.constant.Constant.ERROR_PREFIX;

import java.util.List;
import java.util.Map;

public class Stock {
    private static final String NON_EXIST_PRODUCT_MESSAGE = ERROR_PREFIX + "존재하지 않는 상품입니다. 다시 입력해 주세요.";
    private static final String STOCK_EXCEED_MESSAGE = ERROR_PREFIX + "재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.";

    private final List<Product> stock;

    public Stock(List<Product> stock) {
        this.stock = stock;
    }

    public Product findByName(String name) {
        return stock.stream()
                .filter(product -> product.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(NON_EXIST_PRODUCT_MESSAGE));
    }

    public void validateAll(Map<String, Integer> items) {
        items.forEach(this::validate);
    }

    public void update(Map<Product, Integer> cartItem) {
        cartItem.forEach((product, quantity) -> {
            Product stockProduct = findByName(product.getName());
            if (product.isPromotionEligible()) {
                stockProduct.reducePromotionQuantity(quantity);
            } else {
                stockProduct.reduceQuantity(quantity);
            }
        });
    }

    private void validate(String productName, Integer quantity) {
        if (!isValidName(productName)) {
            throw new IllegalArgumentException(NON_EXIST_PRODUCT_MESSAGE);
        }
        if (!isStockAvailable(productName, quantity)) {
            throw new IllegalArgumentException(STOCK_EXCEED_MESSAGE);
        }
    }

    private boolean isValidName(String name) {
        return stock.stream().anyMatch(product -> product.getName().equals(name));
    }

    private boolean isStockAvailable(String productName, Integer quantity) {
        Product product = findByName(productName);
        return product.isStockAvailable(quantity);
    }

    public List<Product> getStock() {
        return stock.stream().toList();
    }
}
