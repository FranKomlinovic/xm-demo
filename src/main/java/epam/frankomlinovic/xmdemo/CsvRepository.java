package epam.frankomlinovic.xmdemo;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class CsvRepository {

    public List<CsvEntity> loadDataFromCsv(String csvFilePath) {
        FileReader reader;
        try {
            reader = new FileReader(resourceLoader.getResource(getPath(csvFilePath)).getFile());
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, csvFilePath + " Not found", e);
        }
        CsvToBean<CsvEntity> csvToBean = new CsvToBeanBuilder<CsvEntity>(reader)
                .withType(CsvEntity.class).build();

        return csvToBean.parse();
    }

    public Map<String, List<CsvEntity>> loadAllDataFromCsv() {
        Map<String, List<CsvEntity>> map = new HashMap<>();
        Resource[] allResources = loadResources();
        for (Resource resource : allResources) {
            File file;
            try {
                file = resource.getFile();
            } catch (IOException e) {
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR, "Cannot load resources", e);
            }
            FileReader reader;
            try {
                reader = new FileReader(file);
            } catch (FileNotFoundException e) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "File not found", e);
            }
            CsvToBean<CsvEntity> csvToBean = new CsvToBeanBuilder<CsvEntity>(reader)
                    .withType(CsvEntity.class).build();

            map.put(file.getName().split("_")[0], csvToBean.parse());
        }

        return map;
    }

    private Resource[] loadResources() {
        try {
            return ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources("classpath:/crypto/*.csv");
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Cannot load resources", e);
        }
    }

    private ResourceLoader resourceLoader;

    private String getPath(String symbol) {
        return "classpath:crypto/" + symbol + "_values.csv";
    }


}
