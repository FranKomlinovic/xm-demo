package epam.frankomlinovic.xmdemo;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CsvEntity {

    @CsvBindByName
    private Long timestamp;

    @CsvBindByName
    private String symbol;

    @CsvBindByName
    private BigDecimal price;

}
