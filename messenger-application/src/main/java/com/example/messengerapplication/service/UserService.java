package com.example.messengerapplication.service;

import com.example.messengerapplication.DTO.RegisterRequest;
import com.example.messengerapplication.DTO.RegisterResponse;
import com.example.messengerapplication.model.User;
import com.example.messengerapplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

//    public User findByUsername(String email) {
//        return userRepository.findByEmail(email);
//    }

//    public User findByEmail(String email) {
//        return userRepository.findByEmail(email);
//    }

//    public RegisterResponse createUser(RegisterRequest user) {
//        if(user==null){
//            System.out.println("user iis null");
//            return null;
//        }
//        User userEntity = new User();

    //}
}
