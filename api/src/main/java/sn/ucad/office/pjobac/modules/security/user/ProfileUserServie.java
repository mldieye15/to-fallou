package sn.ucad.office.pjobac.modules.security.user;

import sn.ucad.office.pjobac.exception.BusinessResourceException;
import sn.ucad.office.pjobac.modules.security.user.dto.AdminEditRequest;
import sn.ucad.office.pjobac.modules.security.user.dto.AdminResponse;
import sn.ucad.office.pjobac.modules.security.user.dto.UserEditRequest;
import sn.ucad.office.pjobac.modules.security.user.dto.UserResponse;

public interface ProfileUserServie {

    UserResponse majProfileUser(UserEditRequest request)  throws NumberFormatException, BusinessResourceException;
    AdminResponse majProfilAdmin(AdminEditRequest request)  throws NumberFormatException, BusinessResourceException;
}
