package sn.ucad.office.pjobac.modules.security.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import sn.ucad.office.pjobac.modules.etablissement.Etablissement;
import sn.ucad.office.pjobac.modules.fonction.Fonction;
import sn.ucad.office.pjobac.modules.security.role.AppRole;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users",
        uniqueConstraints = {
//                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "code")
        })
public class AppUser {
    @Id
    @Setter(AccessLevel.PROTECTED)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String matricule;

    @NotNull
    @Size(min=2,max=40, message="Prenom doit etre comprise entre 2 et 40 cracteres")
    private String prenoms;

    @NotNull
    @Size(min=2,max=40, message="Nom doit etre comprise entre 2 et 40 cracteres")
    private String nom;

     @NotNull
    private String sexe;

    @NotNull
    private String telephone;

    private Integer anciennete;

//    @ManyToOne
//    private Fonction fonction;

    @ManyToOne
    private Etablissement etablissement;
    //@Size(min=6, max=10, message="username doit etre comprise entre 6 et 10 cracteres")
    @Column(unique = true)
    private String code;

//    @NotNull
//    private Date dateNaiss;

    @NotNull
    private  String banque;

    @NotNull
    @Column(length = 5)
    private  String codeBanque;

    @Column(length = 5)
    @NotNull
    private  int codeAgence;

    @Column(length = 12)
    @NotNull
    private  int numeroCompte;
    @Column(length = 2)
    @NotNull
    private  int cleRib;

    @NotNull
    @Size(min=5, max=100, message="Email doit etre comprise entre 5 et 100 cracteres")
    private String email;

//    @NotNull
//    @Size(min=3, max=50, message="username doit etre comprise entre 3 et 50 cracteres")
//    @Column(unique = true)
//    private String username;

    @NotNull
    @Size(min=6, max=255, message="mot de passe doit etre comprise entre 6 et 255 cracteres")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String mdpasse;

    private String profileImageUrl;

    private boolean isEnabled = true; // par défaut non

    private boolean isLocked = false; // par défaut non

    private boolean accountNonExpired = true; // par défaut oui

    private boolean credentialsNonExpired = true; // par défaut oui

    private boolean accountNonLocked = true; // par défaut oui*/

    private String identifiant;

    @Column(nullable = true)
    private LocalDateTime lastLoginDate;

    @Column(nullable = true)
    private LocalDateTime joinDate = LocalDateTime.now();
    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<AppRole> roles = new ArrayList<>();

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

