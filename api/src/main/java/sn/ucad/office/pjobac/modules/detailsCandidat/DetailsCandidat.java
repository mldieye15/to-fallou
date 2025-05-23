package sn.ucad.office.pjobac.modules.detailsCandidat;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import sn.ucad.office.pjobac.modules.annee.Annee;
import sn.ucad.office.pjobac.modules.security.user.AppUser;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "detailsCandidat"
        ,uniqueConstraints = @UniqueConstraint(columnNames= "numeroCandidat")
)
public class DetailsCandidat{
    @Id
    @Setter(AccessLevel.PROTECTED)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private AppUser candidat;
    @ManyToOne
    private AppUser auteur;
    @ManyToOne
    private Annee annee;
    @Column(columnDefinition = "INT DEFAULT 0")
    @Max(value=100)
    @Min(value = 0)
    private int bonus=0;
    @Column(columnDefinition = "INT DEFAULT 0")
    @Max(value=100)
    @Min(value = 0)
    private int malus=0;
    private int noteFonction;
    private int noteEtablissementProvenance;
    private int noteAnciennete;
    private String numeroCandidat;
    @Column(columnDefinition = "INT DEFAULT 0")
    private int note = 0;
    @Column(columnDefinition = "INT DEFAULT 0")
    @Max(value=60)
    @Min(value = 0)
    private int noteSupervisseur = 0;
    @Column(nullable = true)
    private String appreciation;
    private boolean affectable=false;

    @Column(nullable = true)
    private Long utiCree;
    @Column(updatable = false)
    @CreationTimestamp
    protected LocalDateTime dateCreation = LocalDateTime.now() ;

    protected Long utiModifie;

    @Column(nullable = true)
    @UpdateTimestamp
    protected LocalDateTime dateModification;
}
