package org.chatapp.chatappserver.conversation.dto;

import java.time.Instant;

public record WsNewMessage(
        Long id,
        Long roomId,
        Long senderId,
        Instant createdAt,
        int keyVersion,
        String ciphertextB64,
        String ivB64,
        String aadB64
) {}
