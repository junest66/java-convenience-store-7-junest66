package store.entity;

import java.time.LocalDate;
import java.util.Objects;

public class Promotion {
    private final String name;
    private final int buy;
    private final int get;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public Promotion(String name, int buy, int get, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.buy = buy;
        this.get = get;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public boolean isPromotionValid(LocalDate currentDate) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Promotion promotion = (Promotion) o;
        return buy == promotion.buy &&
                get == promotion.get &&
                Objects.equals(name, promotion.name) &&
                Objects.equals(startDate, promotion.startDate) &&
                Objects.equals(endDate, promotion.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, buy, get, startDate, endDate);
    }
}
