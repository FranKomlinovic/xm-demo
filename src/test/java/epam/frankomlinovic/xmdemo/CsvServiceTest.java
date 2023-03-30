package epam.frankomlinovic.xmdemo;

import epam.frankomlinovic.xmdemo.dto.OldestNewestMinMaxDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class CsvServiceTest {

    @InjectMocks
    private CsvService csvService;

    @Mock
    private CsvRepository mockCsvRepository;

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
}
