package store.entity;

import java.time.LocalDate;

public class Promotion {
    private final String name;
    private final int buy;
    private final int get;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final DateProvider dateProvider;

    public Promotion(String name, int buy, int get, LocalDate startDate, LocalDate endDate, DateProvider dateProvider) {
        this.name = name;
        this.buy = buy;
        this.get = get;
        this.startDate = startDate;
        this.endDate = endDate;
        this.dateProvider = dateProvider;
    }

    public boolean isPromotionValid() {
        LocalDate currentDate = dateProvider.now();
        return !currentDate.isBefore(startDate) && !currentDate.isAfter(endDate);
    }

    public boolean isInsufficientPromotion(Integer value) {
        return value % (buy + get) >= buy;
    }

    public Integer getInsufficientPromotionQuantity(Integer value) {
        return (buy + get) - value % (buy + get);
    }

    public Integer getCycle() {
        return buy + get;
    }

    public String getName() {
        return name;
    }
}
