package org.chatapp.chatappserver.service;

import lombok.RequiredArgsConstructor;
import org.chatapp.chatappserver.domain.dto.SearchUserResponse;
import org.chatapp.chatappserver.domain.model.User;
import org.chatapp.chatappserver.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Override
    public List<SearchUserResponse> searchForUsers(String q) {
        List<User> foundUsers = userRepository.findAllByUsernameStartsWith(q);
        return foundUsers.stream().map(user -> new SearchUserResponse(user.getId(), user.getUsername())).toList();
    }
}
