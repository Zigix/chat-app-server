package org.chatapp.chatappserver.conversation.service;

import lombok.RequiredArgsConstructor;
import org.chatapp.chatappserver.conversation.dto.MyKeyResponse;
import org.chatapp.chatappserver.conversation.dto.UploadRoomKeysRequest;
import org.chatapp.chatappserver.conversation.dto.WsEvent;
import org.chatapp.chatappserver.conversation.entity.RoomKeyEnvelope;
import org.chatapp.chatappserver.conversation.repository.RoomKeyEnvelopeRepository;
import org.chatapp.chatappserver.repository.UserRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class RoomKeyService {
    private final RoomService roomService;
    private final RoomKeyEnvelopeRepository envelopes;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public void uploadKeys(Long myUserId, Long roomId, UploadRoomKeysRequest req) {
        roomService.assertMember(roomId, myUserId);

        for (UploadRoomKeysRequest.KeyItem keyItem : req.keyItems()) {
            roomService.assertMember(roomId, keyItem.userId());
            RoomKeyEnvelope roomKeyEnvelope = new RoomKeyEnvelope();
            roomKeyEnvelope.setRoomId(roomId);
            roomKeyEnvelope.setVersion(req.version());
            roomKeyEnvelope.setForUserId(keyItem.userId());
            roomKeyEnvelope.setWrappedByUserId(req.wrappedByUserId());
            roomKeyEnvelope.setWrappedRoomKeyB64(keyItem.wrappedRoomKeyB64());
            roomKeyEnvelope.setIvB64(keyItem.ivB64());
            roomKeyEnvelope.setAadB64(keyItem.aadB64());
            envelopes.save(roomKeyEnvelope);
        }

        for (UploadRoomKeysRequest.KeyItem keyItem : req.keyItems()) {
            messagingTemplate.convertAndSendToUser(
                    userRepository.findById(keyItem.userId()).get().getUsername(),
                    "/queue/events",
                    new WsEvent("KEY_ENVELOPE_AVAILABLE", Map.of("roomId", roomId, "version", req.version()))
            );
        }
    }

    public MyKeyResponse getMyKey(Long myUserId, Long roomId, int version) {
        roomService.assertMember(roomId, myUserId);
        RoomKeyEnvelope envelope = envelopes.findByRoomIdAndVersionAndForUserId(roomId, version, myUserId)
                .orElseThrow(() -> new NoSuchElementException("No key envelope"));
        return new MyKeyResponse(roomId, version, envelope.getWrappedByUserId(),
                envelope.getWrappedRoomKeyB64(), envelope.getIvB64(), envelope.getAadB64());
    }
}

