package org.chatapp.chatappserver.api;

import lombok.RequiredArgsConstructor;
import org.chatapp.chatappserver.domain.dto.SearchUserResponse;
import org.chatapp.chatappserver.domain.model.User;
import org.chatapp.chatappserver.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(currentUser);
    }

    @GetMapping("/search")
    public ResponseEntity<List<SearchUserResponse>> search(@RequestParam("q") String q) {
        return ResponseEntity.ok(userService.searchForUsers(q));
    }
}
