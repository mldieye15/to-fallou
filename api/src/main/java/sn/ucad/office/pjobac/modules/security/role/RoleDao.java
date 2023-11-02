package sn.ucad.office.pjobac.modules.security.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleDao extends JpaRepository<AppRole, Long> {
    AppRole findByNom(String nom);
}

