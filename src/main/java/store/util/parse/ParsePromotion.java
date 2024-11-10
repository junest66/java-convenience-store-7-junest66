package store.util.parse;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import store.entity.Promotion;

public class ParsePromotion {
    private static final String SEPARATOR = ",";
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static Map<String, Promotion> parse(List<String> promotions) {
        return promotions.stream()
                .skip(1)  // 첫 번째 헤더 부분 제외
                .map(ParsePromotion::parsePromotionLine)
                .collect(Collectors.toMap(Promotion::getName, promotion -> promotion));
    }

    private static Promotion parsePromotionLine(String promotion) {
        String[] fields = promotion.split(SEPARATOR);
        String name = fields[0];
        int buy = Integer.parseInt(fields[1]);
        int get = Integer.parseInt(fields[2]);
        LocalDate startDate = LocalDate.parse(fields[3], dateTimeFormatter);
        LocalDate endDate = LocalDate.parse(fields[4], dateTimeFormatter);
        return new Promotion(name, buy, get, startDate, endDate);
    }
}
