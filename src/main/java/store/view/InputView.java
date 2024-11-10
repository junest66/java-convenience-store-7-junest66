package store.view;

import camp.nextstep.edu.missionutils.Console;

public class InputView {
    private static final String PURCHASE_ASK_MESSAGE = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";

    public static String readItem() {
        System.out.println(PURCHASE_ASK_MESSAGE);
        return Console.readLine();
    }
}
