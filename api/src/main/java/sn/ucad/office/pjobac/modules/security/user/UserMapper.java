package sn.ucad.office.pjobac.modules.security.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import sn.ucad.office.pjobac.config.AppConstants;
import sn.ucad.office.pjobac.modules.security.user.dto.UserAudit;
import sn.ucad.office.pjobac.modules.security.user.dto.UserRequest;
import sn.ucad.office.pjobac.modules.security.user.dto.UserResponse;
import sn.ucad.office.pjobac.utils.AppDateFormatter;

import java.text.ParseException;
import java.util.Date;

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
    @Mapping(source = "request.dateNaissance", target = "dateNaiss", qualifiedByName = "formatStringToDate")
    @Mapping(source = "request.matricule", target = "matricule")
    @Mapping(source = "request.fonction", target = "fonction ", qualifiedByName = "getFonctionById")
    AppUser requestToEntity(UserRequest request);

    @Mapping(source = "request.mdpasse", target = "mdpasse", qualifiedByName = "encodeMdp")
    @Mapping(source = "request.dateNaissance", target = "dateNaiss", qualifiedByName = "formatStringToDate")
    @Mapping(source = "request.matricule", target = "matricule")
    @Mapping(source = "request.fonction", target = "fonction ", qualifiedByName = "getFonctionById")
    AppUser requestToEntiteUp(@MappingTarget AppUser user, UserRequest request);


}
