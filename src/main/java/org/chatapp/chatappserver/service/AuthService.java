package org.chatapp.chatappserver.service;

import org.chatapp.chatappserver.domain.dto.LoginUserRequest;
import org.chatapp.chatappserver.domain.dto.LoginUserResponse;
import org.chatapp.chatappserver.domain.dto.RegisterUserRequest;

public interface AuthService {
    void signUp(RegisterUserRequest registerUserRequest);
    LoginUserResponse login(LoginUserRequest loginUserRequest);
}
