package org.chatapp.chatappserver.conversation.service;

import lombok.RequiredArgsConstructor;
import org.chatapp.chatappserver.conversation.dto.WsSendMessage;
import org.chatapp.chatappserver.conversation.entity.MessageEntity;
import org.chatapp.chatappserver.conversation.repository.MessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final RoomService roomService;
    private final MessageRepository messages;

    @Transactional
    public MessageEntity saveCiphertext(Long senderId, Long roomId, WsSendMessage msg) {
        roomService.assertMember(roomId, senderId);

        MessageEntity entity = new MessageEntity(
                roomId,
                senderId,
                msg.keyVersion(),
                msg.ciphertextB64(),
                msg.ivB64(),
                msg.aadB64(),
                msg.clientMessageId()
        );

        return messages.save(entity);
    }
}

