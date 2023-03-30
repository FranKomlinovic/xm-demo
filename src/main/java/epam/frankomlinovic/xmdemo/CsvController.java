package epam.frankomlinovic.xmdemo;

import epam.frankomlinovic.xmdemo.dto.NormalizedRangeDto;
import epam.frankomlinovic.xmdemo.dto.OldestNewestMinMaxDto;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@AllArgsConstructor
public class CsvController {
    private CsvService csvService;

    @GetMapping("/normalized")
    List<NormalizedRangeDto> allWithNormalizedRange() {
        return csvService.allWithNormalizedRange();
    }

    @GetMapping("/oldestNewestMinMax/{symbol}")
    OldestNewestMinMaxDto oldestNewestMinMaxDto(@PathVariable String symbol) {
        return csvService.calculateOldestNewestMinMax(symbol);
    }

    @GetMapping("/highestNormalized/{date}")
    NormalizedRangeDto normalizedRangeByDay(@PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateTo) {
        return csvService.withNormalizedRangeByDay(dateTo);

    }

}
