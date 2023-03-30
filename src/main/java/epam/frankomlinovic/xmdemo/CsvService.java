package epam.frankomlinovic.xmdemo;

import epam.frankomlinovic.xmdemo.dto.NormalizedRangeDto;
import epam.frankomlinovic.xmdemo.dto.OldestNewestMinMaxDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class CsvService {
    private CsvRepository beanBuilder;
    private Mapper mapper;

    private static final String MESSAGE = "Cannot load resources";

    public OldestNewestMinMaxDto calculateOldestNewestMinMax(String symbol) {
        List<CsvEntity> csvEntities = beanBuilder.loadDataFromCsv(symbol);

        Comparator<CsvEntity> priceComparator = Comparator.comparing(CsvEntity::getPrice);
        Comparator<CsvEntity> timestampComparator = Comparator.comparing(CsvEntity::getTimestamp);

        CsvEntity max = csvEntities.stream().max(priceComparator)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, MESSAGE));

        CsvEntity min = csvEntities.stream().min(priceComparator)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, MESSAGE));

        CsvEntity oldest = csvEntities.stream().min(timestampComparator)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, MESSAGE));

        CsvEntity newest = csvEntities.stream().max(timestampComparator)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, MESSAGE));


        return new OldestNewestMinMaxDto(oldest, newest, min, max);
    }

    public List<NormalizedRangeDto> allWithNormalizedRange() {
        Map<String, List<CsvEntity>> stringListMap = beanBuilder.loadAllDataFromCsv();
        ArrayList<NormalizedRangeDto> normalizedRangeDtos = new ArrayList<>();
        stringListMap.forEach((a, v) -> normalizedRangeDtos.add(mapper.convertToNormalizedRange(a, v)));
        return normalizedRangeDtos.stream().sorted(Comparator.comparing(NormalizedRangeDto::getNormalizedRange).reversed()).toList();
    }

    public NormalizedRangeDto withNormalizedRangeByDay(Date dateTo) {
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(dateTo.toInstant(),
                ZoneId.of("UTC"));
        long startInMilis = zonedDateTime.toInstant().toEpochMilli();
        long endInMilis = zonedDateTime.plusDays(1).toInstant().toEpochMilli();
        Map<String, List<CsvEntity>> stringListMap = beanBuilder.loadAllDataFromCsv();
        ArrayList<NormalizedRangeDto> allRanges = new ArrayList<>();
        stringListMap.forEach((a, b) -> {
            NormalizedRangeDto calculate = calculate(a, b, startInMilis, endInMilis);
            if (calculate != null) {
                allRanges.add(calculate);
            }
        });

        Comparator<NormalizedRangeDto> priceComparator = Comparator.comparing(NormalizedRangeDto::getNormalizedRange);

        if (allRanges.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Cannot find any crypto for given date");
        }
        return allRanges.stream().max(priceComparator).orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, MESSAGE));
    }

    private NormalizedRangeDto calculate(String name, List<CsvEntity> objects, long startInMilis, long endInMilis) {
        List<CsvEntity> csvEntities = objects.stream().filter(a -> startInMilis <= a.getTimestamp() && endInMilis >= a.getTimestamp()).toList();
        if (csvEntities.isEmpty()) {
            return null;
        }
        return mapper.convertToNormalizedRange(name, csvEntities);
    }
}
