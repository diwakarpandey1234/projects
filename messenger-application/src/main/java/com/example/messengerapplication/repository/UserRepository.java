package com.example.messengerapplication.repository;

import com.example.messengerapplication.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    User findByEmail(String email);

    User findByUsernameIgnoreCase(String username);

    boolean existsByUsernameIgnoreCaseAndIdNot(
            String username, Long id);

    Page<User> findByUsernameContainingIgnoreCaseAndIdNot(
            String username,
            Long id,
            Pageable pageable
    );

//    boolean existsByEmailIgnoreCaseAndIdNot(
//            String email, Long id);

  //  boolean existsByUsernameIgnoreCase(String username);

    //boolean existsByEmailIgnoreCase(String email);

   // Optional<User> findByUsernameIgnoreCase(String username);
}
