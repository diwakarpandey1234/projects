package com.example.messengerapplication.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class UserSearchResponseDTO {

    private final Long id;

    private final String username;
}
