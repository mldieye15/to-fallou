package sn.ucad.office.pjobac.modules.security.role;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import sn.ucad.office.pjobac.modules.security.role.dto.RoleAudit;
import sn.ucad.office.pjobac.modules.security.role.dto.RoleRequest;
import sn.ucad.office.pjobac.modules.security.role.dto.RoleResponse;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    // transform the entity to PJO class
    RoleResponse toEntiteResponse(AppRole role);

    // transform the entity to PJO class with audit information
    @Mapping(source = "auteurName", target = "auteur")
    @Mapping(source = "modifName", target = "modificateur")
    RoleAudit toEntiteAudit(AppRole role, String auteurName, String modifName);

    // transform the PJO request to entity
    AppRole requestToEntity(RoleRequest request);

    AppRole requestToEntiteUp(@MappingTarget AppRole role, RoleRequest request);
}