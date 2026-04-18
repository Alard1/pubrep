package input.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;

import dataloader.TxtBean;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class EventBlockBean extends TxtBean {

    @CsvBindByName(column = "id")
    private String id;

    @CsvBindByName(column = "Start")
    @CsvDate(value = "d-M-yyyy")
    private LocalDate start;

    @CsvBindByName(column = "End")
    @CsvDate(value = "d-M-yyyy")
    private LocalDate end;

    @CsvBindByName(column = "html")
    private String html;
}
