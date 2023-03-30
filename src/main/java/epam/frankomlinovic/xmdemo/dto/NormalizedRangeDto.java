package epam.frankomlinovic.xmdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class NormalizedRangeDto {

    private String symbol;
    private BigDecimal normalizedRange;

}
