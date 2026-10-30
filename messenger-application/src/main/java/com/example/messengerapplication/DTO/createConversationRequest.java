package com.example.messengerapplication.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class createConversationRequest {

    @NotNull(message = "Participant user ID is required")
    @Positive(message = "Participant user ID must be positive")
    private Long participantUserId;

}
