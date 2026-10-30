package com.example.messengerapplication.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConversationResponse {

    private Long id;
    private Long participantUserId;
    private String participantUsername;
    private LocalDateTime createdDate;


    public ConversationResponse(ConversationResponse response, boolean b) {
    }
}
