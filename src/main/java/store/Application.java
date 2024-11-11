package store;

import static store.constant.Constant.ERROR_PREFIX;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import store.constant.YesOrNo;
import store.controller.PurchaseController;
import store.dto.StockResponse;
import store.entity.Product;
import store.entity.Promotion;
import store.entity.Stock;
import store.service.CartService;
import store.service.StockService;
import store.util.parse.PromotionParser;
import store.util.parse.ProductParser;
import store.view.InputView;
import store.view.OutputView;

public class Application {
    private static final String PRODUCT_FILE_PATH = "src/main/resources/products.md";
    private static final String PROMOTION_FILE_PATH = "src/main/resources/promotions.md";
    private static final String FILE_ERROR_MESSAGE = ERROR_PREFIX + "파일을 읽는데 실패했습니다.";

    public static void main(String[] args) {
        List<Product> items = getInitProducts();
        Stock stock = new Stock(items);
        StockService stockService = new StockService(stock);
        initializeApplication(stock, stockService);
    }

    private static void initializeApplication(Stock stock, StockService stockService) {
        YesOrNo continueShopping = YesOrNo.YES;
        while (continueShopping == YesOrNo.YES) {
            CartService cartService = new CartService(stock);
            PurchaseController purchaseController = new PurchaseController(cartService, stockService);
            List<StockResponse> stockResult = purchaseController.getStock();
            OutputView.printStartMessage(stockResult);
            purchaseController.purchase();
            continueShopping = InputView.askForAdditionalPurchase();
        }
    }

    private static List<Product> getInitProducts() {
        List<String> products = readFile(PRODUCT_FILE_PATH);
        List<String> promotions = readFile(PROMOTION_FILE_PATH);
        Map<String, Promotion> parsePromotions = PromotionParser.parse(promotions);
        List<Product> items = ProductParser.parse(products, parsePromotions);
        return items;
    }

    private static List<String> readFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            return Files.readAllLines(path);
        } catch (IOException e) {
            throw new UncheckedIOException(FILE_ERROR_MESSAGE, e);
        }
    }
}
