package store.view;

import camp.nextstep.edu.missionutils.Console;
import java.util.List;
import store.constant.YesOrNo;
import store.dto.ProductItem;
import store.util.RetryHandler;

public class InputView {
    private static final String PURCHASE_ASK_MESSAGE = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
    public static final String PROMOTION_ADDITIONAL_ITEM_MESSAGE = "현재 %s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)";
    public static final String NON_PROMOTION_ITEM_MESSAGE = "현재 %s은(는) %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)";
    public static final String MEMBERSHIP_ASK_MESSAGE = "멤버십 할인을 받으시겠습니까? (Y/N)";
    private static final String EXTRA_PURCHASE_ASK_MESSAGE = "감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)";

    public static String readItem() {
        System.out.println(PURCHASE_ASK_MESSAGE);
        return Console.readLine();
    }

    public static List<ProductItem> askForPromotionItems(List<ProductItem> additionalItems) {
        return additionalItems.stream()
                .peek(additionalItem -> System.out.println(String.format(PROMOTION_ADDITIONAL_ITEM_MESSAGE,
                        additionalItem.productName(), additionalItem.quantity())))
                .filter(additionalItem -> inputYesOrNo().equals(YesOrNo.YES))
                .toList();
    }

    public static List<ProductItem> askForNonPromotionItems(List<ProductItem> nonPromotionItems) {
        return nonPromotionItems.stream()
                .peek(nonPromotionItem -> System.out.println(String.format(NON_PROMOTION_ITEM_MESSAGE,
                        nonPromotionItem.productName(), nonPromotionItem.quantity())))
                .filter(nonPromotionItem -> inputYesOrNo().equals(YesOrNo.NO))
                .toList();
    }

    public static boolean askForMembership() {
        System.out.println(MEMBERSHIP_ASK_MESSAGE);
        String input = Console.readLine();
        return YesOrNo.of(input).equals(YesOrNo.YES);
    }

    public static YesOrNo askForAdditionalPurchase() {
        System.out.println(EXTRA_PURCHASE_ASK_MESSAGE);
        return RetryHandler.handleWithRetry(() -> inputYesOrNo());
    }

    private static YesOrNo inputYesOrNo() {
        return YesOrNo.of(Console.readLine());
    }
}
