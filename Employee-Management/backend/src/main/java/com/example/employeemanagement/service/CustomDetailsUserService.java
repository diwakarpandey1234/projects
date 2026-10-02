package com.example.employeemanagement.service;



import com.example.employeemanagement.repository.userDetailsRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomDetailsUserService implements UserDetailsService {


    private final userDetailsRepository UserDetailsRepository;
    CustomDetailsUserService(userDetailsRepository UserDetailsRepository){
        this.UserDetailsRepository=UserDetailsRepository;
    }

    @Override
    public UserDetails loadUserByUsername(@NonNull String userName) throws UsernameNotFoundException {
        return  UserDetailsRepository.findByUserName(userName).
                orElseThrow(()->
                        new UsernameNotFoundException("User Not Found"));
    }
}
