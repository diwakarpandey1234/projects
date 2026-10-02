package com.example.employeemanagement.service;



import com.example.employeemanagement.entity.Role;
import com.example.employeemanagement.entity.User;
import com.example.employeemanagement.repository.userDetailsRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminUserInitializer {
    @Bean
    public CommandLineRunner CreateAdminUser(userDetailsRepository UserDetailsRepository, PasswordEncoder passwordEncoder){
        return args -> {
            if(UserDetailsRepository.findByUserName("admin").isEmpty()){

                User admin=new User();
                admin.setUserName("admin");
                admin.setPassword(passwordEncoder.encode("admin12"));
                admin.setRole(Role.valueOf("ADMIN"));

                UserDetailsRepository.save(admin);
                System.out.println("Default admin  created");

            }

            if(UserDetailsRepository.findByUserName("hr").isEmpty()){

                User admin=new User();
                admin.setUserName("hr");
                admin.setPassword(passwordEncoder.encode("hr12"));
                admin.setRole(Role.valueOf("HR"));

                UserDetailsRepository.save(admin);
                System.out.println("Default  HR created");

            }


        if(UserDetailsRepository.findByUserName("user").isEmpty()){

            User user=new User();
            user.setUserName("user");
            user.setPassword(passwordEncoder.encode("user12"));
            user.setRole(Role.valueOf("USER"));

            UserDetailsRepository.save(user);
            System.out.println("Default  user created");

        }
    };

    }

}

