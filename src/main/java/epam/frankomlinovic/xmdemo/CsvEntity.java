package epam.frankomlinovic.xmdemo;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CsvEntity {

    @CsvBindByName
    private Long timestamp;

    @CsvBindByName
    private String symbol;

    @CsvBindByName
    private BigDecimal price;

}
