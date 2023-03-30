package epam.frankomlinovic.xmdemo;

import epam.frankomlinovic.xmdemo.dto.NormalizedRangeDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
class MapperTest {

    @Autowired
    private Mapper mapper;

    private static final String name = "BTC";

    @Test
    void convertToNormalizedRangeTestSuccess() {

        List<CsvEntity> csvEntities = List.of(new CsvEntity(243523L, name, BigDecimal.valueOf(100)), new CsvEntity(4253L, name, BigDecimal.valueOf(50)));
        NormalizedRangeDto btc = mapper.convertToNormalizedRange(name, csvEntities);

        Assertions.assertEquals(name, btc.getSymbol());
        Assertions.assertEquals(BigDecimal.valueOf(1.0), btc.getNormalizedRange());


    }

    @Test
    void convertToNormalizedRangeTestFail() {
        List<CsvEntity> csvEntities = List.of();
        Assertions.assertThrows(ResponseStatusException.class, () -> mapper.convertToNormalizedRange(name, csvEntities));
    }

}
