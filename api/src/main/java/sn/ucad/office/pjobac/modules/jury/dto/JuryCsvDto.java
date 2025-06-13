package sn.ucad.office.pjobac.modules.jury.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class JuryCsvDto {
    @CsvBindByName(column = "numero", required = true)
    private String numero;

    @CsvBindByName(column = "centreLibelleLong", required = true)
    private String centreLibelleLong;
}
