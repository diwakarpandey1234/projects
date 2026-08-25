package com.watermark.platform.repository;

import com.watermark.platform.entity.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(@NonNull String userName);


}
