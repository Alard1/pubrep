package input.model;

import com.opencsv.bean.CsvBindByName;

import dataloader.TxtBean;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CharacteristicBean extends TxtBean {

    @CsvBindByName(column = "ID")
    private String id;

    @CsvBindByName(column = "characteristic")
    private String characteristic;

    @CsvBindByName(column = "value")
    private String value;
}
