package org.chatapp.chatappserver.conversation.repository;

import org.chatapp.chatappserver.conversation.entity.RoomKeyEnvelope;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomKeyEnvelopeRepository extends JpaRepository<RoomKeyEnvelope, Long> {
    Optional<RoomKeyEnvelope> findByRoomIdAndVersionAndForUserId(Long roomId, int version, Long forUserId);
}
