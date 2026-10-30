package com.example.messengerapplication.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {

    @NotBlank(message = "username should be valid")
    @Size(max=50, message = "username not exceed 50")
    private String username;
    @NotBlank(message = "Enter valid Password")
    private String password;
}
