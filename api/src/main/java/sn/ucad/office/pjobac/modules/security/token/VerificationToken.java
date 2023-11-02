package sn.ucad.office.pjobac.modules.security.token;

import jakarta.persistence.*;
import lombok.*;
import sn.ucad.office.pjobac.modules.security.user.AppUser;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tokens")
public class  VerificationToken {

    @Id
    @Setter(AccessLevel.PROTECTED)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne(fetch = FetchType.LAZY)
    private AppUser user;

    @Column(nullable = true)
    private Instant expiryDate;
}
