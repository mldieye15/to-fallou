package sn.ucad.office.pjobac.modules.detailsCandidat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DetailsCandidatNoteRequest {
    private  int noteSupervisseur;
    private String appreciation;
}