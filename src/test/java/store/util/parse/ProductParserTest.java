package store.util.parse;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import store.entity.Product;
import store.entity.Promotion;

class ProductParserTest {

    private static final List<String> productData = List.of(
            "name,price,quantity,promotion",
            "콜라,1000,10,탄산2+1",
            "콜라,1000,10,null",
            "사이다,1000,8,탄산2+1",
            "사이다,1000,7,null",
            "오렌지주스,1800,9,MD추천상품",
            "탄산수,1200,5,탄산2+1",
            "물,500,10,null",
            "비타민워터,1500,6,null",
            "감자칩,1500,5,반짝할인",
            "감자칩,1500,5,null",
            "초코바,1200,5,MD추천상품",
            "초코바,1200,5,null",
            "에너지바,2000,5,null",
            "정식도시락,6400,8,null",
            "컵라면,1700,1,MD추천상품",
            "컵라면,1700,10,null"
    );

    private static final Map<String, Promotion> promotions = Map.of(
            "탄산2+1", new Promotion("탄산2+1", 2, 1, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31)),
            "MD추천상품", new Promotion("MD추천상품", 1, 1, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31)),
            "반짝할인", new Promotion("반짝할인", 1, 1, LocalDate.of(2024, 11, 1), LocalDate.of(2024, 11, 30))
    );

    @Test
    void 상품_파싱_테스트() {
        List<Product> products = ProductParser.parse(productData, promotions);
        assertThat(products).hasSize(11);
    }
}
