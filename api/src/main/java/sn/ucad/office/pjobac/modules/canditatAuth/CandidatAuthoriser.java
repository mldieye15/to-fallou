package sn.ucad.office.pjobac.modules.canditatAuth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import sn.ucad.office.pjobac.modules.etablissement.Etablissement;
import sn.ucad.office.pjobac.modules.security.role.AppRole;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "candidatAuthorisers",
        uniqueConstraints = {
//                @UniqueConstraint(columnNames = "username"),
//                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "code")
        })
public class CandidatAuthoriser {
    @Id
    @Setter(AccessLevel.PROTECTED)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String code;

    @NotNull
    private String matricule;

    @NotNull
    @Size(min=2,max=40, message="Prenom doit etre comprise entre 2 et 40 cracteres")
    private String prenoms;

    @NotNull
    @Size(min=2,max=40, message="Nom doit etre comprise entre 2 et 40 cracteres")
    private String nom;

    @NotNull
    private String telephone;
    @NotNull
    private String etablisement;

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

