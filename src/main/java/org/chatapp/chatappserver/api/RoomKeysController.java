package org.chatapp.chatappserver.api;

import jakarta.validation.Valid;
import org.chatapp.chatappserver.conversation.dto.MyKeyResponse;
import org.chatapp.chatappserver.conversation.dto.UploadRoomKeysRequest;
import org.chatapp.chatappserver.conversation.service.RoomKeyService;
import org.chatapp.chatappserver.domain.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rooms/{roomId}")
public class RoomKeysController {
    private final RoomKeyService roomKeyService;

    public RoomKeysController(RoomKeyService roomKeyService) {
        this.roomKeyService = roomKeyService;
    }

    @PostMapping("/keys/bulk")
    public ResponseEntity<String> upload(@PathVariable Long roomId,
                                         @RequestBody @Valid UploadRoomKeysRequest req,
                                         @AuthenticationPrincipal User user) {
        roomKeyService.uploadKeys(user.getId(), roomId, req);
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/my-key")
    public MyKeyResponse myKey(@PathVariable Long roomId,
                               @RequestParam int version,
                               @AuthenticationPrincipal User user) {
        return roomKeyService.getMyKey(user.getId(), roomId, version);
    }
}