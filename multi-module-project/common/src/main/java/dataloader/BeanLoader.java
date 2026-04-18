package dataloader;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.BeanVerifier;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class BeanLoader {

    private char separator = '\t';
    private boolean ignoreQuotations = true;
    private int skipLines = 0;

    public BeanLoader() {}

    public <T extends TxtBean> List<T> txtFileToBean(
            String fileLocation, Class<T> clazz, BeanVerifier<T> headerVerifier) {

        Path path = java.nio.file.Paths.get(fileLocation);

        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.ISO_8859_1)) {

            CSVParser parser =
                    new CSVParserBuilder()
                            .withSeparator(separator)
                            .withEscapeChar('\0')
                            .withIgnoreQuotations(ignoreQuotations)
                            .build();

            CSVReader csvReader =
                    new CSVReaderBuilder(reader)
                            .withSkipLines(skipLines)
                            .withCSVParser(parser)
                            .build();

            CsvToBean<T> cb =
                    new CsvToBeanBuilder<T>(csvReader)
                            .withType(clazz)
                            .withQuoteChar('"')
                            .withVerifier(headerVerifier)
                            .build();
            return cb.parse();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public <T extends TxtBean> List<T> txtFileToBean(String fileLocation, Class<T> clazz) {
        return this.txtFileToBean(fileLocation, clazz, null);
    }

    public char getSeparator() {
        return separator;
    }

    public void setSeparator(char separator) {
        this.separator = separator;
    }

    public boolean isIgnoreQuotations() {
        return ignoreQuotations;
    }

    public void setIgnoreQuotations(boolean ignoreQuotations) {
        this.ignoreQuotations = ignoreQuotations;
    }

    public int getSkipLines() {
        return skipLines;
    }

    public void setSkipLines(int skipLines) {
        this.skipLines = skipLines;
    }
}
