package sn.ucad.office.pjobac.modules.etablissement.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.UpdateTimestamp;
import sn.ucad.office.pjobac.modules.typeEtablissement.TypeEtablissement;
import sn.ucad.office.pjobac.modules.ville.Ville;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EtablissementResponse {
    private Long id;
    private String libelleLong;
    private String libelleCourt;
    private TypeEtablissement typeEtablissement;
    private Ville ville;
    protected LocalDateTime dateCreation ;
    protected LocalDateTime dateModification;
}
