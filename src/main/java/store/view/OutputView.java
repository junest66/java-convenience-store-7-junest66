package store.view;

import java.util.List;
import store.dto.ProductItem;
import store.dto.Receipt;
import store.dto.StockResponse;

public class OutputView {
    private static final String START_MESSAGE = "안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다.\n";
    private static final String PROMOTION_IN_STOCK_FORMAT = "- %s %,d원 %s %s";
    private static final String REGULAR_IN_STOCK_FORMAT = "- %s %,d원 %s";
    private static final String RECEIPT_HEADER = "==============W 편의점================";
    private static final String RECEIPT_ITEM_HEADER = String.format("%-17s %-9s %-1s", "상품명", "수량", "금액");
    private static final String NAME_QUANTITY_PRICE = "%-17s %-9d %,d";
    private static final String RECEIPT_PROMOTION_HEADER = "=============증    정===============";
    private static final String NAME_QUANTITY_ONLY = "%-17s %d";
    private static final String RECEIPT_FOOTER = "====================================";
    private static final String NAME_AMOUNT_ONLY = "%-27s -%,d";
    private static final String TOTAL_AMOUNT_LABEL = "총구매액";
    private static final String EVENT_DISCOUNT_LABEL = "행사할인";
    private static final String MEMBERSHIP_DISCOUNT_LABEL = "멤버십할인";
    private static final String FINAL_PAYMENT_LABEL = "내실돈";

    public static void printStartMessage(List<StockResponse> stock) {
        System.out.println(START_MESSAGE);
        for (StockResponse stockResponse : stock) {
            if (!stockResponse.promotionName().isBlank()) {
                printStockWithPromotion(stockResponse);
                continue;
            }
            printStockWithoutPromotion(stockResponse);
        }
        System.out.println();
    }

    public static void printErrorMessage(String errorMessage) {
        System.out.println(errorMessage);
    }

    private static void printStockWithPromotion(StockResponse stockResponse) {
        System.out.println(
                String.format(PROMOTION_IN_STOCK_FORMAT, stockResponse.name(), stockResponse.price(),
                        stockResponse.promotionQuantity(),
                        stockResponse.promotionName()));
        System.out.println(String.format(REGULAR_IN_STOCK_FORMAT, stockResponse.name(), stockResponse.price(),
                stockResponse.quantity()));
    }

    private static void printStockWithoutPromotion(StockResponse stockResponse) {
        System.out.println(String.format(REGULAR_IN_STOCK_FORMAT, stockResponse.name(), stockResponse.price(),
                stockResponse.quantity()));
    }

    public static void printReceipt(Receipt receipt) {
        System.out.println(RECEIPT_HEADER);
        printPurchaseProduct(receipt);
        printPresent(receipt);
        printAmount(receipt);
    }

    private static void printPurchaseProduct(Receipt receipt) {
        System.out.println(RECEIPT_ITEM_HEADER);
        for (int i = 0; i < receipt.purchaseProducts().size(); i++) {
            System.out.println(String.format(NAME_QUANTITY_PRICE,
                    receipt.purchaseProducts().get(i).productName(),
                    receipt.purchaseProducts().get(i).quantity(),
                    receipt.purchaseProducts().get(i).quantity() * receipt.purchaseProducts().get(i).price()
            ));
        }
    }

    private static void printPresent(Receipt receipt) {
        List<ProductItem> discountedItems = receipt.promotionDiscountedItems();
        if (!discountedItems.isEmpty()) {
            System.out.println(RECEIPT_PROMOTION_HEADER);
            discountedItems.forEach(
                    item -> System.out.println(String.format(NAME_QUANTITY_ONLY, item.productName(), item.quantity())));
        }
        System.out.println(RECEIPT_FOOTER);
    }

    private static void printAmount(Receipt receipt) {
        System.out.printf(NAME_QUANTITY_PRICE + "\n", TOTAL_AMOUNT_LABEL,
                receipt.purchaseProducts().stream().mapToInt(productItem -> productItem.quantity()).sum(),
                receipt.amount().totalAmount());
        System.out.printf(NAME_AMOUNT_ONLY + "\n", EVENT_DISCOUNT_LABEL, receipt.amount().promotionDiscountAmount());
        System.out.printf(NAME_AMOUNT_ONLY + "\n", MEMBERSHIP_DISCOUNT_LABEL,
                receipt.amount().membershipDiscountAmount());
        System.out.printf(NAME_AMOUNT_ONLY + "\n", FINAL_PAYMENT_LABEL, receipt.amount().finalAmount());
    }
}
