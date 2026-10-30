package com.example.messengerapplication.model;



import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Data
@Getter
@Setter
@Table(name="users", uniqueConstraints = {
        @UniqueConstraint(
                name = "users_username",
                columnNames = "username"),

        @UniqueConstraint(
                name = "users_email",
                columnNames = "email")
}

)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String username;


    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = false, length = 50)
    private String password;

    @Column(nullable = false)
    private LocalDateTime createdAt=LocalDateTime.now();


}
