package org.chatapp.chatappserver.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.chatapp.chatappserver.conversation.dto.ConversationDto;
import org.chatapp.chatappserver.conversation.dto.CreateDmRequest;
import org.chatapp.chatappserver.conversation.dto.CreateDmResponse;
import org.chatapp.chatappserver.conversation.service.RoomService;
import org.chatapp.chatappserver.domain.model.User;
import org.chatapp.chatappserver.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomsController {

    private final RoomService roomService;
    private final UserService userService;

    @PostMapping("/dm")
    public CreateDmResponse createDm(@RequestBody @Valid CreateDmRequest req,
                                     Principal principal) {
        Long myUserId = ((User) userService.loadUserByUsername(principal.getName())).getId();
        return roomService.createOrGetDm(myUserId, req.otherUserId());
    }

    @GetMapping("/recent")
    public ResponseEntity<List<ConversationDto>> recentConversations(Principal principal) {
        List<ConversationDto> recent = roomService.getRecentConversations(principal);
        return ResponseEntity.ok(recent);
    }
}
