package sn.ucad.office.pjobac.modules.demande;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import sn.ucad.office.pjobac.modules.academie.Academie;
import sn.ucad.office.pjobac.modules.centre.Centre;
import sn.ucad.office.pjobac.modules.etatDemande.EtatDemande;
import sn.ucad.office.pjobac.modules.jury.Jury;
import sn.ucad.office.pjobac.modules.security.user.AppUser;
import sn.ucad.office.pjobac.modules.session.Session;
import sn.ucad.office.pjobac.modules.ville.Ville;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Demande {
    @Id
    @Setter(AccessLevel.PROTECTED)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private AppUser user;

    @ManyToOne
    private Ville ville;
    @ManyToOne
    private Academie academie;
    @ManyToOne
    private Session session;
    @ManyToOne
    private Centre centre;
    @OneToOne
    private Jury jury;
    @ManyToOne
    private EtatDemande etatDemande;
//    private Integer ordreArrivee;
    private boolean proposition;
    private Integer rang;
    protected LocalDateTime dateDemande=LocalDateTime.now();
    protected LocalDateTime dateRejetDemande;
    protected LocalDateTime dateConfirmationDemande;

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