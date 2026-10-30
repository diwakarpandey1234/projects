package com.example.messengerapplication.service;

import com.example.messengerapplication.DTO.UpdateProfileRequest;
import com.example.messengerapplication.DTO.UserProfileResponse;
import com.example.messengerapplication.model.User;
import com.example.messengerapplication.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserProfileService {

    private final UserRepository userRepository;

    public UserProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserProfileResponse updateUserProfile(String authenticatedUsername,
                                                 UpdateProfileRequest updateProfileRequest){

        User user = userRepository
                .findByUsernameIgnoreCase(authenticatedUsername);
//                .orElseThrow(() -> new ResponseStatusException(
//                        HttpStatus.NOT_FOUND, "User not found"));


        String newUsername=updateProfileRequest.getUsername();

        if (userRepository.existsByUsernameIgnoreCaseAndIdNot(
                newUsername, user.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Username is already taken");
        }

        user.setUsername(newUsername);

        try{
            User savedUser = userRepository.saveAndFlush(user);

            return new UserProfileResponse(
                    savedUser.getId(),
                    savedUser.getUsername(),
                    savedUser.getUsername(),
                    savedUser.getCreatedAt()
            );
        }catch(DataIntegrityViolationException e){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Username or email is already in use");
        }

    }
}
