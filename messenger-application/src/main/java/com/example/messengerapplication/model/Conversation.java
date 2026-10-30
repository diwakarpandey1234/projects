package com.example.messengerapplication.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name="converstaions",
        uniqueConstraints={
         @UniqueConstraint(
                 name="uk_conversation_user_pair",
                 columnNames ={
                         "user_one_id","user_two_id"
                 })})
@AllArgsConstructor
@NoArgsConstructor
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name="user_one_id",nullable=false)
    private User userOne;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name="user_two_id",nullable=false)
    private User userTwo;

    @Column(nullable=false,updatable=false)
    private LocalDateTime createdAt;

    public Conversation(User userOne, User userTwo) {
        this.userOne = userOne;
        this.userTwo = userTwo;
    }


    @PrePersist
    public void onCreate(){
        this.createdAt = LocalDateTime.now();
    }


}
