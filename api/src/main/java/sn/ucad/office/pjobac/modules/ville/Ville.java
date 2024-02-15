package sn.ucad.office.pjobac.modules.ville;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import sn.ucad.office.pjobac.modules.academie.Academie;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ville"
        , uniqueConstraints = {
        @UniqueConstraint(columnNames="libelleLong"),
        @UniqueConstraint(columnNames="libelleCourt")

}
)
public class Ville {
    @Id
    @Setter(AccessLevel.PROTECTED)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String libelleLong;

    @NotNull
    private String libelleCourt;
    @Column(columnDefinition = "INT DEFAULT 0")
    private int totalJury=0;
    @ManyToOne(fetch = FetchType.EAGER)
    private Academie academie;
    @Column(columnDefinition = "INT DEFAULT 0")
    private int totalDemandes=0;
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
