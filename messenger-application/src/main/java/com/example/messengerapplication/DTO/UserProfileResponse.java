package com.example.messengerapplication.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class UserProfileResponse {

    private final Long id;
    private final String name;
    private final String email;
    private final LocalDateTime createdAt;


}
