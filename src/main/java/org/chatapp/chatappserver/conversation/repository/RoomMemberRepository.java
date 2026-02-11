package org.chatapp.chatappserver.conversation.repository;

import org.chatapp.chatappserver.conversation.entity.RoomMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomMemberRepository extends JpaRepository<RoomMember, Long> {
    boolean existsByRoomIdAndUserId(Long roomId, Long userId);
    List<RoomMember> findByRoomId(Long roomId);
}
