package org.chatapp.chatappserver.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.chatapp.chatappserver.domain.dto.LoginUserRequest;
import org.chatapp.chatappserver.domain.dto.LoginUserResponse;
import org.chatapp.chatappserver.domain.dto.RegisterUserRequest;
import org.chatapp.chatappserver.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@Valid @RequestBody RegisterUserRequest registerUserRequest) {
        authService.signUp(registerUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginUserResponse> login(@RequestBody LoginUserRequest loginUserRequest) {
        return ResponseEntity.ok(authService.login(loginUserRequest));
    }
}
