package epam.frankomlinovic.xmdemo;

import epam.frankomlinovic.xmdemo.dto.NormalizedRangeDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Component
public class Mapper {

    public NormalizedRangeDto convertToNormalizedRange(String name, List<CsvEntity> values) {
        BigDecimal min = values.stream().min(Comparator.comparing(CsvEntity::getPrice))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot load resources"))
                .getPrice();
        BigDecimal max = values.stream().max(Comparator.comparing(CsvEntity::getPrice))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot load resources"))
                .getPrice();

        BigDecimal subtract = max.subtract(min);
        return new NormalizedRangeDto(name, BigDecimal.valueOf(subtract.doubleValue() / min.doubleValue()));

    }

}
