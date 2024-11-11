package store.entity;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;

public class RealDateProvider implements DateProvider {
    @Override
    public LocalDate now() {
        return DateTimes.now().toLocalDate();
    }
}
