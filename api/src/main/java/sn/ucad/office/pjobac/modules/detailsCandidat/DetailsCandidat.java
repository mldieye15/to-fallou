package sn.ucad.office.pjobac.modules.detailsCandidat;

import jakarta.persistence.*;
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
        //, uniqueConstraints = @UniqueConstraint(columnNames={"libelle", "sigle"})
)
public class DetailsCandidat{
    @Id
    @Setter(AccessLevel.PROTECTED)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private AppUser candidat;

    @ManyToOne
    private Annee annee;
    @Column(columnDefinition = "INT DEFAULT 0")
    private int bonus=0;
    @Column(columnDefinition = "INT DEFAULT 0")
    private int malus=0;

    private int noteFonction;
    private int noteEtablissementProvenance;
    private int noteAnciennete;
    @Column(columnDefinition = "INT DEFAULT 0")
    private int note = 0;
    @Column(columnDefinition = "INT DEFAULT 0")
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
