package epam.frankomlinovic.xmdemo.dto;

import epam.frankomlinovic.xmdemo.CsvEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OldestNewestMinMaxDto {
    private CsvEntity oldest;
    private CsvEntity newest;
    private CsvEntity min;
    private CsvEntity max;
}
