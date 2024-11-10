package store.util.validator;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PurchaseItemValidatorTest {
    @Test
    void 구매할상품이_여러개일때_예외발생하지_않는다() {
        String[] splitItems = new String[]{"[콜라-1]", "[사이다-2]"};
        assertDoesNotThrow(() -> PurchaseItemValidator.validateSize(splitItems));
    }

    @Test
    void 구매할상품이_없을때_예외발생() {
        String[] splitItems = new String[0];
        assertThatThrownBy(() -> PurchaseItemValidator.validateSize(splitItems))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"[콜라-1]", "[사이다-2]", "[콜 라-10]"})
    void 구매할상품이_형식이_올바를때_예외가_발생하지않는다(String value) {
        assertDoesNotThrow(() -> PurchaseItemValidator.validateFormat(value));
    }

    @ParameterizedTest
    @ValueSource(strings = {"[콜라-1", "사이다-2]", "[콜라-0]", "[콜라-1.1]", "[콜라1]", "[콜-라1]", "[ -1]"})
    void 구매할상품이_형식이_올바르지_않을떄_예외가_발생한다(String value) {
        assertThatThrownBy(() -> PurchaseItemValidator.validateFormat(value))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "2", "10", "1000"})
    void 수량이_숫자형태_이면서_범위가_올바를때_예외가_발생하지_않는다(String value) {
        assertDoesNotThrow(() -> PurchaseItemValidator.validateQuantity(value));
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "1001", "-1"})
    void 수량이_범위가_맞지않을때_예외가_발생한다(String value) {
        assertThatThrownBy(() -> PurchaseItemValidator.validateQuantity(value))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"asd", "1.3", "3a"})
    void 수량이_자연수_형태가_아닐떄(String value) {
        assertThatThrownBy(() -> PurchaseItemValidator.validateQuantity(value))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
