package sn.ucad.office.pjobac.modules.session;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import sn.ucad.office.pjobac.modules.annee.Annee;
import sn.ucad.office.pjobac.modules.typeSession.TypeSession;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        //, uniqueConstraints = @UniqueConstraint(columnNames={"libelle", "sigle"})
)
public class Session {
    @Id
    @Setter(AccessLevel.PROTECTED)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String libelleLong;

    @NotNull
    private Date dateDebut;

    @NotNull
    private Date dateFin;

    @NotNull
    private Integer nombreDemandeAutorise;

    @NotNull
    private Integer delaisValidation;

    @NotNull
    private Date dateOuvertureDepotCandidature;

    @NotNull
    private Date dateClotureDepotCandidature;

    @ManyToOne(fetch = FetchType.EAGER)
    private Annee annee;

    @ManyToOne
    private TypeSession typeSession;

    private  boolean ouvert=false;
    private  boolean modification;
    private  boolean candidature=false;
    private boolean phaseTwo=false;
    @Column(nullable = true)
    private Long utiCree;


    @Column(updatable = false)
    @CreationTimestamp
    protected LocalDateTime dateCreation = LocalDateTime.now() ;

    protected Long utiModifie;

    @Column(nullable = true)
    @UpdateTimestamp
    protected LocalDateTime dateModification;
    @AssertTrue(message = "La date de debut doit être antérieure à la date de fin")
    private boolean isDateDebutBeforeDateFin() {
        return dateDebut.before(dateFin);
    }

    @AssertTrue(message = "La date d'ouverture de candidature doit être incluse entre la date de début et la date de fin")
    private boolean isDateOuvertureCandidature() {
        return dateDebut.before(dateOuvertureDepotCandidature) && dateOuvertureDepotCandidature.before(dateFin);
    }

    @AssertTrue(message = "La date de cloture de candidature doit être incluse entre la date de début et la date de fin")
    private boolean isDateClotureDepotCandidatureValid() {
        return dateDebut.before(dateClotureDepotCandidature) && dateClotureDepotCandidature.before(dateFin);
    }

    @AssertTrue(message = "La date d'ouverture de candidature doit être antérieure à la date de cloture de candidature")
    private boolean isDateOuvertureBeforeCloture() {
        return dateOuvertureDepotCandidature.before(dateClotureDepotCandidature);
    }
}
