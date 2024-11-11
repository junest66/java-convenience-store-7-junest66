package store.entity;

import java.time.LocalDate;

public class TestDateProvider implements DateProvider {
    private LocalDate date;

    public TestDateProvider(LocalDate date) {
        this.date = date;
    }

    @Override
    public LocalDate now() {
        return date;
    }
}
