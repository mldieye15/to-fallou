package sn.ucad.office.pjobac;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import sn.ucad.office.pjobac.modules.security.role.RoleService;
import sn.ucad.office.pjobac.modules.security.role.dto.RoleRequest;
import sn.ucad.office.pjobac.modules.security.token.AuthService;
import sn.ucad.office.pjobac.modules.security.user.UserService;
import sn.ucad.office.pjobac.modules.security.user.dto.UserRequest;

@SpringBootApplication
public class DemandeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemandeServiceApplication.class, args);
    }


    @Bean
    CommandLineRunner run(RoleService roleService, UserService userService, AuthService authService){
        return args -> {
//            roleService.add(new RoleRequest("ROLE_USER"));
//            roleService.add(new RoleRequest("ROLE_PLANIFICATEUR"));
//            roleService.add(new RoleRequest("ROLE_SUPERVISEUR"));
//            roleService.add(new RoleRequest("ROLE_ADMIN"));
//            roleService.add(new RoleRequest("ROLE_SUPER_ADMIN"));

//            userService.add(new UserRequest("laminedev@gmail.com", "mldieye", "password", "", true, false));
//
//            authService.inscrire(new UserRequest("Lamine","DIEYE","laminedev@gmail.com", "mldieye", "password", "", true, false));
//
//            userService.addRoleToUser("mldieye", "ROLE_SUPER_ADMIN");
//            userService.addRoleToUser("mldieye", "ROLE_ADMIN");
//            userService.addRoleToUser("mldieye", "ROLE_USER");

        };
    };

}
