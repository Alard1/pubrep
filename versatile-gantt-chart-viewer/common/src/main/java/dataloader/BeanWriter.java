package dataloader;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

public class BeanWriter {

    private char separator = '\t';

    public <T extends TxtBean> void writeRowsToCsv(
            Path filePath, List<T> rows, Class<? extends T> clazz) {

        try (Writer writer = Files.newBufferedWriter(filePath); ) {
            StatefulBeanToCsv<T> beanToCsv =
                    new StatefulBeanToCsvBuilder<T>(writer)
                            .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                            .withSeparator(separator)
                            .build();

            beanToCsv.write(rows);
        } catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
