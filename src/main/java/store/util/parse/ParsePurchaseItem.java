package store.util.parse;

import java.util.LinkedHashMap;
import java.util.Map;
import store.util.validator.PurchaseItemValidator;

public class ParsePurchaseItem {
    private static final String ITEM_SEPARATOR = ",";
    private static final String ITEM_REGEX = "[\\[\\]]";
    private static final String ITEM_QUANTITY_SEPARATOR = "-";

    public static Map<String, Integer> parse(String items) {
        Map<String, Integer> result = new LinkedHashMap<>();
        String[] splitItems = items.split(ITEM_SEPARATOR);
        PurchaseItemValidator.validateSize(splitItems);
        for (String item : splitItems) {
            processItem(item, result);
        }
        return result;
    }

    private static void processItem(String item, Map<String, Integer> result) {
        PurchaseItemValidator.validateFormat(item);
        String cleanedItem = item.replaceAll(ITEM_REGEX, "");
        String[] parts = cleanedItem.split(ITEM_QUANTITY_SEPARATOR);
        int quantity = PurchaseItemValidator.validateQuantity(parts[1]);
        result.merge(parts[0], quantity, Integer::sum);
    }
}
