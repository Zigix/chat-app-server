package org.chatapp.chatappserver.service;

import lombok.RequiredArgsConstructor;
import org.chatapp.chatappserver.config.jwt.JwtTokenUtil;
import org.chatapp.chatappserver.domain.dto.*;
import org.chatapp.chatappserver.domain.exception.ValidationException;
import org.chatapp.chatappserver.domain.model.User;
import org.chatapp.chatappserver.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    @Transactional
    public void signUp(RegisterUserRequest registerUserRequest) {
        validateUserRequest(registerUserRequest);
        User user = mapToUser(registerUserRequest);

        userRepository.save(user);
    }

    @Override
    public LoginUserResponse login(LoginUserRequest loginUserRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginUserRequest.getUsername(),
                loginUserRequest.getPassword()
        );

        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authenticationManager.authenticate(authenticationToken);

        User user = (User) auth.getPrincipal();
        String accessToken = jwtTokenUtil.generateAccessToken(user);
        String refreshToken = jwtTokenUtil.generateRefreshToken(user);

        LoginUserResponse response = new LoginUserResponse();
        VaultDto vaultDto = new VaultDto();

        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setPubEcdhJwk(user.getPubEcdhJwk());

        vaultDto.setVersion(user.getVaultVersion());
        vaultDto.setKdfSaltB64(user.getVaultSaltBase64());
        vaultDto.setKdfIterations(user.getVaultIterations());
        vaultDto.setWrappedMkB64(user.getWrappedMkB64());
        vaultDto.setWrappedMkIvB64(user.getWrappedMkIvB64());
        vaultDto.setWrappedEcdhPrivB64(user.getWrappedEcdhPrivB64());
        vaultDto.setWrappedEcdhPrivIvB64(user.getWrappedEcdhPrivIvB64());

        response.setVaultDto(vaultDto);
        return response;
    }

    private User mapToUser(RegisterUserRequest registerUserRequest) {
        User user = new User();
        user.setEmail(registerUserRequest.getEmail());
        user.setUsername(registerUserRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerUserRequest.getPassword()));
        user.setPubEcdhJwk(registerUserRequest.getPubEcdhJwk());

        VaultDto vaultDto = registerUserRequest.getVault();
        user.setVaultVersion(vaultDto.getVersion());
        user.setVaultSaltBase64(vaultDto.getKdfSaltB64());
        user.setVaultIterations(vaultDto.getKdfIterations());
        user.setWrappedMkB64(vaultDto.getWrappedMkB64());
        user.setWrappedMkIvB64(vaultDto.getWrappedMkIvB64());
        user.setWrappedEcdhPrivB64(vaultDto.getWrappedEcdhPrivB64());
        user.setWrappedEcdhPrivIvB64(vaultDto.getWrappedEcdhPrivIvB64());

        return user;
    }

    private void validateUserRequest(RegisterUserRequest registerUserRequest) {
        List<FieldErrorDto> errorsList = new ArrayList<>();

        if (userRepository.existsByEmail(registerUserRequest.getEmail())) {
            errorsList.add(new FieldErrorDto("email", "Email already exists"));
        }

        if (userRepository.existsByUsername(registerUserRequest.getUsername())) {
            errorsList.add(new FieldErrorDto("username", "Username already exists"));
        }

        if (!registerUserRequest.getPassword().equals(registerUserRequest.getRePassword())) {
            errorsList.add(new FieldErrorDto("rePassword", "Passwords do not match"));
        }

        if (!errorsList.isEmpty()) {
            throw new ValidationException("Validation failed", errorsList);
        }
    }
}
