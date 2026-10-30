package com.example.messengerapplication.service;


import com.example.messengerapplication.DTO.UserSearchResponseDTO;
import com.example.messengerapplication.model.User;
import com.example.messengerapplication.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserSearchService {

    private final UserRepository userRepository;

    public UserSearchService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public Page<UserSearchResponseDTO> searchUsers(String authenticatedUsername,
                                              String query,
                                              int pageNumber,
                                              int pageSize){

        if(query == null || query.length()<2){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Search query must contain at least 2 characters");
        }

        if(pageNumber==0 || pageSize<1 || pageSize>50){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Page size must be between 1 and 50");
        }

        User currentUser = userRepository
                .findByUsernameIgnoreCase(authenticatedUsername);

        Pageable pageable = PageRequest.of(pageNumber-1, pageSize);

        Page<User> users = userRepository
                .findByUsernameContainingIgnoreCaseAndIdNot(
                        query.trim(),
                        currentUser.getId(),
                        pageable
                );


        return users.map(user ->
                new UserSearchResponseDTO(user.getId(), user.getUsername())
        );
    }
}
