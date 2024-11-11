package store.util.validator;

import static store.constant.Constant.ERROR_PREFIX;

public class PurchaseItemValidator {

    private static final String GENERAL_ERROR_MESSAGE = "잘못된 입력입니다. 다시 입력해 주세요.";
    private static final String FORMAT_ERROR_MESSAGE = "올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.";
    //맨앞과 맨뒤는 대괄호이면서 상품명에 공백이 아닌, 공백이 포함된 문자열과 1 이상의 숫자형태의 수량가 '-'으로 연결 된 문자열
    private static final String PURCHASE_REGEX = "\\[[A-Za-z가-힣]+(?:\\s+[A-Za-z가-힣]+)*-[1-9]\\d*\\]";
    private static final int MAX_QUANTITY = 1000;

    public static void validateSize(String[] splitItems) {
        if (splitItems.length == 0) {
            throw new IllegalArgumentException(ERROR_PREFIX + GENERAL_ERROR_MESSAGE);
        }
    }

    public static void validateFormat(String item) {
        if (!item.matches(PURCHASE_REGEX)) {
            throw new IllegalArgumentException(ERROR_PREFIX + FORMAT_ERROR_MESSAGE);
        }
    }

    public static int validateQuantity(String quantity) {
        int parseQuantity = validateNumeric(quantity);
        if (parseQuantity <= 0 || parseQuantity > MAX_QUANTITY) {
            throw new IllegalArgumentException(ERROR_PREFIX + GENERAL_ERROR_MESSAGE);
        }
        return parseQuantity;
    }

    private static int validateNumeric(String part) {
        try {
            return Integer.parseInt(part);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ERROR_PREFIX + FORMAT_ERROR_MESSAGE);
        }
    }
}
