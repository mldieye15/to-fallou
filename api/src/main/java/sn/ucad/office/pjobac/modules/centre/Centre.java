package sn.ucad.office.pjobac.modules.centre;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import sn.ucad.office.pjobac.modules.typeCentre.TypeCentre;
import sn.ucad.office.pjobac.modules.ville.Ville;

import java.time.LocalDateTime;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table( name = "centre"
        , uniqueConstraints = { @UniqueConstraint(columnNames="libelleLong"),
                                @UniqueConstraint(columnNames="libelleCourt")

}
)
public class Centre {
    @Id
    @Setter(AccessLevel.PROTECTED)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String libelleLong;

    @NotNull
    private String libelleCourt;
    @Column(columnDefinition = "INT DEFAULT 0")
    private int nombreJury=0;
    @ManyToOne(fetch = FetchType.EAGER)
    private Ville ville;

    @ManyToOne(fetch = FetchType.EAGER)
    private TypeCentre typeCentre;


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
