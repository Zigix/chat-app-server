package org.chatapp.chatappserver.conversation.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    private String name;

    @Enumerated(EnumType.STRING)
    private RoomType type;

    private int currentKeyVersion = 1;

    public Room(Long roomId, RoomType type) {
        this.roomId = roomId;
        this.type = type;
    }

    public Room(RoomType type) {
        this.type = type;
    }
}
