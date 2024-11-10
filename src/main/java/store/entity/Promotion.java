package store.entity;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;

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

    public boolean isPromotionValid() {
        LocalDate currentDate = DateTimes.now().toLocalDate();
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
