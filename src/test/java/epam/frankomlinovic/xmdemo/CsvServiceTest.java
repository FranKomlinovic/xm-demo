package epam.frankomlinovic.xmdemo;

import epam.frankomlinovic.xmdemo.dto.NormalizedRangeDto;
import epam.frankomlinovic.xmdemo.dto.OldestNewestMinMaxDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class CsvServiceTest {

    @InjectMocks
    private CsvService csvService;

    @Mock
    private CsvRepository mockCsvRepository;

    @Mock
    private Mapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void calculateOldestNewestMinMax() {
        CsvEntity highest = new CsvEntity(1564654L, "BTC", BigDecimal.valueOf(800.0));
        CsvEntity lowest = new CsvEntity(1564654L, "BTC", BigDecimal.valueOf(50.0));
        CsvEntity newest = new CsvEntity(1564654435L, "BTC", BigDecimal.valueOf(100.0));
        CsvEntity oldest = new CsvEntity(15654L, "BTC", BigDecimal.valueOf(200.0));
        CsvEntity regular = new CsvEntity(1564654L, "BTC", BigDecimal.valueOf(300.0));

        List<CsvEntity> entities = Arrays.asList(highest, oldest, lowest, regular, newest);

        when(mockCsvRepository.loadDataFromCsv(any())).thenReturn(entities);

        OldestNewestMinMaxDto result = csvService.calculateOldestNewestMinMax("BTC");

        assertNotNull(result);
        assertEquals(oldest, result.getOldest());
        assertEquals(newest, result.getNewest());
        assertEquals(lowest, result.getMin());
        assertEquals(highest, result.getMax());
    }

    @Test
    void calculateOldestNewestMinMaxThrowsException() {
        List<CsvEntity> entities = new ArrayList<>();
        when(mockCsvRepository.loadDataFromCsv(any())).thenReturn(entities);

        assertThrows(ResponseStatusException.class, () -> csvService.calculateOldestNewestMinMax("BTC"));
    }

    @Test
    void testAllWithNormalizedRange() {
        // Arrange
        List<CsvEntity> btcEntities = new ArrayList<>();
        List<CsvEntity> ethEntities = new ArrayList<>();
        btcEntities.add(new CsvEntity(1000L, "BTC", BigDecimal.valueOf(600)));
        btcEntities.add(new CsvEntity(1000L, "BTC", BigDecimal.valueOf(500)));
        ethEntities.add(new CsvEntity(1000L, "ETH", BigDecimal.valueOf(700)));
        ethEntities.add(new CsvEntity(1000L, "ETH", BigDecimal.valueOf(500)));
        Map<String, List<CsvEntity>> data = new HashMap<>();
        data.put("BTC", btcEntities);
        data.put("ETH", ethEntities);
        Mockito.when(mockCsvRepository.loadAllDataFromCsv()).thenReturn(data);

        Mockito.when(mapper.convertToNormalizedRange("BTC", btcEntities)).thenReturn(new NormalizedRangeDto("BTC", BigDecimal.valueOf(0.60)));
        Mockito.when(mapper.convertToNormalizedRange("ETH", ethEntities)).thenReturn(new NormalizedRangeDto("ETH", BigDecimal.valueOf(0.80)));

        // Act
        List<NormalizedRangeDto> result = csvService.allWithNormalizedRange();

        // Assert
        assertEquals(2, result.size());
        assertEquals("ETH", result.get(0).getSymbol());
        assertEquals(BigDecimal.valueOf(0.80), result.get(0).getNormalizedRange());
        assertEquals("BTC", result.get(1).getSymbol());
        assertEquals(BigDecimal.valueOf(0.60), result.get(1).getNormalizedRange());
    }

    @Test
    void testWithNormalizedRangeByDay() {
        // Given
        Date dateTo = new Date(LocalDate.of(2022, 1, 1).atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli());
        List<CsvEntity> entities = new ArrayList<>();
        entities.add(new CsvEntity(1641002400000L, "BTC", BigDecimal.valueOf(50000.0)));
        entities.add(new CsvEntity(1641002400000L, "ETH", BigDecimal.valueOf(50000.0)));
        Map<String, List<CsvEntity>> stringListMap = new HashMap<>();
        stringListMap.put("BTC", Collections.singletonList(entities.get(0)));
        stringListMap.put("ETH", Collections.singletonList(entities.get(1)));
        Mockito.when(mockCsvRepository.loadAllDataFromCsv()).thenReturn(stringListMap);
        NormalizedRangeDto expectedBitcoinDto = new NormalizedRangeDto("BTC", BigDecimal.valueOf(2.0));
        NormalizedRangeDto expectedEtherDto = new NormalizedRangeDto("ETH", BigDecimal.valueOf(1.0));
        Mockito.when(mapper.convertToNormalizedRange("BTC", Collections.singletonList(entities.get(0)))).thenReturn(expectedEtherDto);
        Mockito.when(mapper.convertToNormalizedRange("ETH", Collections.singletonList(entities.get(1)))).thenReturn(expectedBitcoinDto);

        // When
        NormalizedRangeDto actualDto = csvService.withNormalizedRangeByDay(dateTo);

        // Then
        Assertions.assertEquals(expectedBitcoinDto, actualDto);
    }
}
