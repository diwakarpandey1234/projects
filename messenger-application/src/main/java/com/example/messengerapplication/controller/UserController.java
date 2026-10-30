package com.example.messengerapplication.controller;

import com.example.messengerapplication.DTO.RegisterRequest;
import com.example.messengerapplication.DTO.UserProfileResponse;
import com.example.messengerapplication.DTO.UserSearchResponseDTO;
import com.example.messengerapplication.model.User;
import com.example.messengerapplication.repository.UserRepository;
import com.example.messengerapplication.service.UserSearchService;
import com.example.messengerapplication.service.UserService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    private final UserService userService;

    private final UserSearchService userSearchService;
    @Autowired
    private final UserRepository userRepository;

    public UserController(UserService userService, UserSearchService userSearchService, UserRepository userRepository) {
        this.userService = userService;
        this.userSearchService = userSearchService;
        this.userRepository = userRepository;
    }

    @GetMapping("/me")
    public UserProfileResponse getMyProfile(Authentication authentication) {
        String username=authentication.name();
        User user=userRepository.findByUsernameIgnoreCase(username);
        if(user==null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username not found");
        }

        return new UserProfileResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }

    @GetMapping("/search")
    public Page<UserSearchResponseDTO> searchUser(Authentication authentication,
                                                  String query,
                                                  int page, int size
                                                 ){

        return userSearchService.searchUsers(
                authentication.name(),
                query,
                page,
                size
        );

    }


}
