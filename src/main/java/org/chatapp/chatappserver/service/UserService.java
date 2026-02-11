package org.chatapp.chatappserver.service;

import org.chatapp.chatappserver.domain.dto.SearchUserResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<SearchUserResponse> searchForUsers(String q);
}
