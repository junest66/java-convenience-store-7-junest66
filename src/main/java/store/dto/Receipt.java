package store.dto;

import java.util.List;

public record Receipt(List<ProductItemWithPrice> purchaseProducts, List<ProductItem> promotionDiscountedItems, Amount amount) {
}
