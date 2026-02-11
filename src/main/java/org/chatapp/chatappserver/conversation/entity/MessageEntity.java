package org.chatapp.chatappserver.conversation.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "messages", indexes = {
        @Index(name = "ix_messages_room_created", columnList = "room_id,created_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_id", nullable = false)
    private Long roomId;

    @Column(name = "sender_id", nullable = false)
    private Long senderId;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "key_version", nullable = false)
    private int keyVersion;

    @Lob
    @Column(name = "ciphertext_b64", nullable = false)
    private String ciphertextB64;

    @Column(name = "iv_b64", nullable = false, length = 200)
    private String ivB64;

    @Lob
    @Column(name = "aad_b64", nullable = false)
    private String aadB64;

    @Column(name = "client_message_id", nullable = false, length = 100)
    private Long clientMessageId;

    public MessageEntity(Long roomId, Long senderId, int i, @NotBlank String s, @NotBlank String s1, @NotBlank String s2, @NotBlank String s3) {
        this.roomId = roomId;
        this.senderId = senderId;
        this.keyVersion = i;
        this.ciphertextB64 = s;
        this.ivB64 = s1;
        this.aadB64 = s2;
        this.clientMessageId = Long.parseLong(s3);
    }
}

