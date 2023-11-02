package sn.ucad.office.pjobac.modules.security.token;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sn.ucad.office.pjobac.modules.security.user.AppUser;

@Mapper(componentModel = "spring")
public interface VerificationTokenMapper {
    @Mapping(source = "user", target = "user")
    @Mapping(target = "token", expression = "java(java.util.UUID.randomUUID().toString())")
    VerificationToken getVerifToken(AppUser user);

}
