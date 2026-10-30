package com.example.messengerapplication.repository;


import com.example.messengerapplication.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository  extends JpaRepository<Conversation,Long> {
    Optional<Conversation> findByUserOne_IdAndUserTwo_Id(
            Long userOneId,
            Long userTwoId
    );

    List<Conversation> findByUserOne_IdOrUserTwo_Id(
            Long userOneId,
            Long userTwoId
    );
}
