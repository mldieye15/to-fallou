package sn.ucad.office.pjobac.modules.canditatAuth.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data
public class CandidatCSV {
    @CsvBindByName(column = "code")
    private String code;

    @CsvBindByName(column = "matricule")
    private String matricule;

    @CsvBindByName(column = "prenoms")
    private String prenoms;

    @CsvBindByName(column = "nom")
    private String nom;

    @CsvBindByName(column = "telephone")
    private String telephone;

    @CsvBindByName(column = "etablisement")
    private String etablisement;
}
