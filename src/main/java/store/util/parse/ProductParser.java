package store.util.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import store.entity.DateProvider;
import store.entity.Product;
import store.entity.Promotion;
import store.entity.RealDateProvider;

public class ProductParser {
    private static final DateProvider dateProvider = new RealDateProvider();

    public static List<Product> parse(List<String> products, Map<String, Promotion> promotions) {
        List<Product> stockProduct = new ArrayList<>();
        for (int i = 1; i < products.size(); i++) { // 헤더 부분 제외
            String[] fields = products.get(i).split(",");
            int price = Integer.parseInt(fields[1]);
            int quantity = Integer.parseInt(fields[2]);
            Promotion promotion = promotions.get(fields[3]);
            addOrUpdateProduct(stockProduct, fields[0], price, quantity, promotion);
        }
        return stockProduct;
    }

    private static void addOrUpdateProduct(List<Product> products, String name, int price, int quantity,
                                           Promotion promotion) {
        findProduct(products, name).ifPresentOrElse(
                existingProduct -> existingProduct.addQuantity(quantity, promotion),
                () -> products.add(new Product(name, price, quantity, promotion, dateProvider))
        );
    }

    private static Optional<Product> findProduct(List<Product> products, String name) {
        return products.stream()
                .filter(p -> p.getName().equals(name))
                .findFirst();
    }
}
