package store.view;

import camp.nextstep.edu.missionutils.Console;
import java.util.List;
import store.constant.YesOrNo;
import store.dto.ProductItem;

public class InputView {
    private static final String PURCHASE_ASK_MESSAGE = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
    public static final String PROMOTION_ADDITIONAL_ITEM_MESSAGE = "현재 %s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)";

    public static String readItem() {
        System.out.println(PURCHASE_ASK_MESSAGE);
        return Console.readLine();
    }

    public static List<ProductItem> askForPromotionItems(List<ProductItem> additionalItems) {
        return additionalItems.stream()
                .peek(additionalItem -> System.out.println(String.format(PROMOTION_ADDITIONAL_ITEM_MESSAGE,
                        additionalItem.productName(), additionalItem.quantity())))
                .filter(additionalItem -> {
                    String input = Console.readLine();
                    return YesOrNo.of(input).equals(YesOrNo.YES);
                })
                .toList();
    }
}
