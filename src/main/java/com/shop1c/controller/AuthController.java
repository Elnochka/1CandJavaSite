package com.shop1c.controller;


import com.shop1c.entity.dto.LoginDto;
import com.shop1c.entity.dto.RegisterDto;
import com.shop1c.entity.dto.UserDto;
import com.shop1c.service.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "API для реєстрації та логіну користувачів")
public class AuthController {

    private final UsersService usersService;

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody RegisterDto dto) {

        log.info("Register user. userId={}", dto.getEmail());
        return usersService.register(dto);

    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginDto dto) {

        log.info("Login user. userId={}", dto.getEmail());
        return usersService.login(dto);

    }

    @PostMapping("/refresh")
    public Map<String, String> refresh(@RequestBody Map<String, String> body) {

        log.info("Refresh user.");
        return usersService.refresh(body.get("refresh"));

    }

    @Operation(summary = "Отримати user за email (DTO)")
    @GetMapping("/{email}")
    public ResponseEntity<UserDto> getByEmail(@PathVariable String email) {

        log.info("Get user by email. userId={}", email);
        return usersService.getByEmail(email)
                .map(users -> ResponseEntity.ok(UserDto.fromEntity(users)))
                .orElse(ResponseEntity.notFound().build());

    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> me(@RequestHeader("Authorization") String authHeader) {
        return usersService.getMe(authHeader)
                .map(users -> ResponseEntity.ok(UserDto.fromEntity(users)))
                .orElse(ResponseEntity.notFound().build());
    }

}
