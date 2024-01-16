package sn.ucad.office.pjobac.modules.security.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;
import sn.ucad.office.pjobac.modules.security.user.dto.*;

@Mapper(componentModel = "spring", uses = {UserMapperUtil.class})
@Component
public interface UserMapper {

    // transform the entity to PJO class
    UserResponse toEntiteResponse(AppUser user);

    // transform the entity to PJO class with audit information
    @Mapping(source = "auteurName", target = "auteur")
    @Mapping(source = "modifName", target = "modificateur")
    UserAudit toEntiteAudit(AppUser user, String auteurName, String modifName);

    // transform the PJO request to entity
    //@Mapping(source = "request.dateNaissance", target = "dateNaissance", qualifiedByName = "formatStringToDate")
    @Mapping(source = "request.mdpasse", target = "mdpasse", qualifiedByName = "encodeMdp")
    @Mapping(source = "request.dateNaiss", target = "dateNaiss", qualifiedByName = "formatStringToDate")
    @Mapping(source = "request.matricule", target = "matricule")
    @Mapping(source = "request.fonction", target = "fonction ", qualifiedByName = "getFonctionById")
    @Mapping(source ="request.etablissement", target = "etablissement", qualifiedByName = "getEtablissementById")
    AppUser requestToEntity(UserRequest request);

    @Mapping(source = "request.mdpasse", target = "mdpasse", qualifiedByName = "encodeMdp")
    @Mapping(source = "request.dateNaiss", target = "dateNaiss", qualifiedByName = "formatStringToDate")
    @Mapping(source = "request.matricule", target = "matricule")
    @Mapping(source = "request.fonction", target = "fonction ", qualifiedByName = "getFonctionById")
    @Mapping(source ="request.etablissement", target = "etablissement", qualifiedByName = "getEtablissementById")
    AppUser requestToEntiteUp(@MappingTarget AppUser user, UserRequest request);
    AdminResponse userToAdminResponse(AppUser user);
    @Mapping(source = "request.mdpasse", target = "mdpasse", qualifiedByName = "encodeMdp")
    @Mapping(source = "request.dateNaiss", target = "dateNaiss", qualifiedByName = "formatStringToDate")
    AppUser adminRequestToUser(AdminRequest request);
    @Mapping(source = "request.dateNaiss", target = "dateNaiss", qualifiedByName = "formatStringToDate")
    AppUser adminRequestToUserUp(@MappingTarget AppUser user,AdminEditRequest request);


}
