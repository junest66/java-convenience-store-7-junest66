package store.dto;

public record Amount(int totalAmount, int promotionDiscountAmount, int membershipDiscountAmount, int finalAmount) {
}
