package org.chatapp.chatappserver.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.chatapp.chatappserver.conversation.dto.WsEvent;
import org.chatapp.chatappserver.conversation.dto.WsNewMessage;
import org.chatapp.chatappserver.conversation.dto.WsSendMessage;
import org.chatapp.chatappserver.conversation.entity.MessageEntity;
import org.chatapp.chatappserver.conversation.service.MessageService;
import org.chatapp.chatappserver.domain.model.User;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatWsController {
    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/rooms/{roomId}/send")
    public void send(@DestinationVariable Long roomId,
                     @Payload @Valid WsSendMessage msg,
                     @AuthenticationPrincipal User user) {

        Long senderId = user.getId();
        MessageEntity saved = messageService.saveCiphertext(senderId, roomId, msg);

        WsNewMessage payload = new WsNewMessage(
                saved.getId(),
                roomId,
                senderId,
                saved.getCreatedAt(),
                saved.getKeyVersion(),
                saved.getCiphertextB64(),
                saved.getIvB64(),
                saved.getAadB64()
        );

        messagingTemplate.convertAndSend(
                "/topic/rooms/" + roomId,
                new WsEvent("NEW_MESSAGE", payload)
        );
    }
}
