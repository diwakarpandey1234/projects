package com.example.messengerapplication.service;


import com.example.messengerapplication.DTO.ConversationResponse;
import com.example.messengerapplication.model.Conversation;
import com.example.messengerapplication.model.User;
import com.example.messengerapplication.repository.ConversationRepository;
import com.example.messengerapplication.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;


@Service
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    public ConversationService(ConversationRepository conversationRepository, UserRepository userRepository) {
        this.conversationRepository = conversationRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public CreateResult createConversation(
            String authenticatedUsername,
            Long participantUserId
    ){

        User currentUser = userRepository.findByUsernameIgnoreCase(authenticatedUsername);

        if(currentUser == null){throw new ResponseStatusException(HttpStatus.BAD_REQUEST);}


        if (currentUser.getId().equals(participantUserId)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "You cannot create a conversation with yourself");
        }

        Optional<User> participant = Optional.of(userRepository.
                findById(participantUserId).
                orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Participant not found")));
//        if(participant == null){
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Participant not found");
//        }

        User userOne;
        User userTwo;
        if (currentUser.getId() < participant.get().getId()) {
            userOne = currentUser;
            userTwo = participant.get();
        } else {
            userOne = participant.get();
            userTwo = currentUser;
        }

        var existingConversation=conversationRepository.findByUserOne_IdAndUserTwo_Id(userOne.getId(),userTwo.getId());

        if(existingConversation.isPresent()){
            return new CreateResult(
                    toResponse(existingConversation.get(),currentUser.getId()),false
            );
        }

        Conversation conversation = new Conversation(userOne, userTwo);
        Conversation saved = conversationRepository.save(conversation);

        return new CreateResult(toResponse(saved, currentUser.getId()),
                true);
    }


    private ConversationResponse toResponse(Conversation conversation, Long currentUserId) {
        User otherUser=conversation.getUserOne().
                getId().equals(currentUserId)?
                conversation.getUserTwo():
                conversation.getUserOne();

        return new ConversationResponse(
                conversation.getId(),
                otherUser.getId(),
                otherUser.getUsername(),
                conversation.getCreatedAt()
        );
    }


    public record CreateResult(
            ConversationResponse conversation,
            boolean created) {
    }

}
