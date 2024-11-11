package store.service;

import java.util.List;
import store.dto.StockResponse;
import store.entity.Product;
import store.entity.Stock;

public class StockService {
    private final Stock stock;

    public StockService(Stock stock) {
        this.stock = stock;
    }

    public List<StockResponse> getStock() {
        List<Product> products = stock.getStock();
        return products.stream()
                .map(product -> StockResponse.from(product))
                .toList();
    }
}
