package sn.ucad.office.pjobac.modules.jury;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import sn.ucad.office.pjobac.modules.centre.Centre;
import sn.ucad.office.pjobac.modules.session.Session;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        //, uniqueConstraints = @UniqueConstraint(columnNames={"libelle", "sigle"})
)
public class Jury {
    @Id
    @Setter(AccessLevel.PROTECTED)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String numero;
    @NotNull
    private String nom;
    @ManyToOne(fetch = FetchType.EAGER)
    private Centre centre;
    @ManyToOne(fetch = FetchType.EAGER)
    private Session session;
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
