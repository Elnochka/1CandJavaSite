package com.shop1c.service;

import com.shop1c.entity.Users;
import com.shop1c.entity.dto.LoginDto;
import com.shop1c.entity.dto.RegisterDto;
import com.shop1c.model.RoleEnum;
import com.shop1c.repository.UsersRepository;
import com.shop1c.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public Map<String, String> register(RegisterDto dto) {

        if (usersRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("User with email already exists: " + dto.getEmail());
        }

        Users user = new Users();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setCode1C(dto.getCode1C());
        user.setName1C(dto.getName1C());
        user.setRole(dto.getRole() != null ? dto.getRole() : RoleEnum.CUSTOMER);
        usersRepository.save(user);

        String access = jwtService.generateAccessToken(user);
        String refresh = jwtService.generateRefreshToken(user);

        log.info("Registered user successfully. ");

        return Map.of("access", access, "refresh", refresh);
    }

    public Map<String, String> login(LoginDto dto) {
        Users user = usersRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {

            log.debug("Bad credentials. ");
            throw new RuntimeException("Bad credentials");

        }

        log.info("Login user successfully. ");

        return Map.of(
                "access", jwtService.generateAccessToken(user),
                "refresh", jwtService.generateRefreshToken(user)
        );
    }

    public Map<String, String> refresh(String refreshToken) {

        String username = jwtService.extractUsername(refreshToken);
        Users user = usersRepository.findByEmail(username).orElseThrow();

        if (!jwtService.isValid(refreshToken, user)) {

            log.debug("Invalid refresh token");
            throw new RuntimeException("Invalid refresh token");

        }

        String newAccess = jwtService.generateAccessToken(user);

        log.info("Refresh user successfully. ");

        return Map.of("access", newAccess);
    }

    public Optional<Users> getByEmail(String email) {

        log.info("Got user by email successfully. userId={}", email);
        return usersRepository.findByEmail(email);

    }

    public Optional<Users> getMe(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing Authorization header");
        }

        String token = authorizationHeader.substring(7);
        String email = jwtService.extractUsername(token);

        return Optional.ofNullable(usersRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found")));
    }



}
