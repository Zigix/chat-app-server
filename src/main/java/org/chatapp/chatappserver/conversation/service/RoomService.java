package org.chatapp.chatappserver.conversation.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.chatapp.chatappserver.conversation.dto.ConversationDto;
import org.chatapp.chatappserver.conversation.dto.CreateDmResponse;
import org.chatapp.chatappserver.conversation.entity.Room;
import org.chatapp.chatappserver.conversation.entity.RoomMember;
import org.chatapp.chatappserver.conversation.entity.RoomType;
import org.chatapp.chatappserver.conversation.repository.RoomMemberRepository;
import org.chatapp.chatappserver.conversation.repository.RoomRepository;
import org.chatapp.chatappserver.domain.model.User;
import org.chatapp.chatappserver.service.UserService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository rooms;
    private final RoomMemberRepository members;
    private final UserService userService;

    @Transactional
    public CreateDmResponse createOrGetDm(Long myUserId, Long otherUserId) {
        Optional<Room> roomOptional = rooms.findDirectMessageRoomForTwoUsers(RoomType.DIRECT_MESSAGE, myUserId, otherUserId);

        if (roomOptional.isPresent()) {
            Room room = roomOptional.get();
            return new CreateDmResponse(room.getRoomId(), room.getCurrentKeyVersion());
        }

        Room room = new Room(RoomType.DIRECT_MESSAGE);
        rooms.save(room);

        members.save(new RoomMember(room.getRoomId(), myUserId));
        members.save(new RoomMember(room.getRoomId(), otherUserId));

        return new CreateDmResponse(room.getRoomId(), room.getCurrentKeyVersion());
    }

    public void assertMember(Long roomId, Long userId) {
        if (!members.existsByRoomIdAndUserId(roomId, userId)) {
            throw new AccessDeniedException("Not a room member");
        }
    }

    public List<Long> listMemberIds(Long roomId) {
        return members.findByRoomId(roomId).stream().map(RoomMember::getUserId).toList();
    }

    public List<ConversationDto> getRecentConversations(Principal principal) {
        Long userId = ((User) userService.loadUserByUsername(principal.getName())).getId();
        return rooms.findByMemberUserId(userId)
                .stream()
                .map(c -> new ConversationDto(c.getRoomId(), c.getName()))
                .toList();
    }
}

