package store.controller;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import store.dto.ProductItem;
import store.dto.StockResponse;
import store.service.CartService;
import store.service.StockService;
import store.util.parse.ParsePurchaseItem;
import store.view.InputView;
import store.view.OutputView;

public class PurchaseController {
    private final CartService cartService;
    private final StockService stockService;

    public PurchaseController(CartService cartService, StockService stockService) {
        this.cartService = cartService;
        this.stockService = stockService;
    }

    public List<StockResponse> getStock() {
        return stockService.getStock();
    }

    public void purchase() {
        inputPurchaseItem();
        inputAdditionalPromotionItem();
        inputPromotionStock();
        OutputView.printReceipt(cartService.pay(inputMembership()));
    }

    private void inputPromotionStock() {
        handleWithRetry(() -> {
            List<ProductItem> nonPromotionItems = cartService.getNonPromotion();
            if (!nonPromotionItems.isEmpty()) {
                List<ProductItem> removeNonPromotionItems = InputView.askForNonPromotionItems(nonPromotionItems);
                cartService.removeNonPromotionItems(removeNonPromotionItems);
            }
        });
    }

    private void inputPurchaseItem() {
        handleWithRetry(() -> {
            String items = InputView.readItem();
            Map<String, Integer> parseItems = ParsePurchaseItem.parse(items);
            cartService.purchase(parseItems);
        });
    }

    private void inputAdditionalPromotionItem() {
        handleWithRetry(() -> {
            List<ProductItem> additionalItems = cartService.getPromotionAdditionalItems();
            if (!additionalItems.isEmpty()) {
                List<ProductItem> items = InputView.askForPromotionItems(additionalItems);
                cartService.addPromotionItems(items);
            }
        });
    }

    private boolean inputMembership() {
        return handleWithRetry(() -> InputView.askForMembership());
    }

    private void handleWithRetry(Runnable action) {
        while (true) {
            try {
                action.run();
                break;
            } catch (IllegalArgumentException e) {
                OutputView.printErrorMessage(e.getMessage());
            }
        }
    }

    private <T> T handleWithRetry(Supplier<T> action) {
        while (true) {
            try {
                return action.get();
            } catch (IllegalArgumentException e) {
                OutputView.printErrorMessage(e.getMessage());
            }
        }
    }
}
