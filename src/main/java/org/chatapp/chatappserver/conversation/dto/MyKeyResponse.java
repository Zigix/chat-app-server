package org.chatapp.chatappserver.conversation.dto;

public record MyKeyResponse(
        Long roomId,
        int version,
        Long wrappedByUserId,
        String wrappedRoomKeyB64,
        String ivB64,
        String aadB64
) {}