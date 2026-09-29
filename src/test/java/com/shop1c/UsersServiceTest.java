package com.shop1c;

import com.shop1c.entity.Users;
import com.shop1c.entity.dto.LoginDto;
import com.shop1c.entity.dto.RegisterDto;
import com.shop1c.model.RoleEnum;
import com.shop1c.repository.UsersRepository;
import com.shop1c.security.JwtService;
import com.shop1c.service.UsersService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsersServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UsersService usersService;

    @Test
    void register_shouldCreateUserAndReturnTokens() {

        RegisterDto dto = RegisterDto.builder()
                .email("user@gmail.com")
                .password("password123")
                .code1C("C001")
                .name1C("John")
                .role(RoleEnum.CUSTOMER)
                .build();

        when(usersRepository.existsByEmail("user@gmail.com"))
                .thenReturn(false);

        when(passwordEncoder.encode("password123"))
                .thenReturn("encodedPassword");

        when(jwtService.generateAccessToken(any(Users.class)))
                .thenReturn("access-token");

        when(jwtService.generateRefreshToken(any(Users.class)))
                .thenReturn("refresh-token");

        Map<String, String> result =
                usersService.register(dto);

        assertThat(result.get("access"))
                .isEqualTo("access-token");

        assertThat(result.get("refresh"))
                .isEqualTo("refresh-token");

        ArgumentCaptor<Users> captor =
                ArgumentCaptor.forClass(Users.class);

        verify(usersRepository)
                .save(captor.capture());

        Users savedUser = captor.getValue();

        assertThat(savedUser.getEmail())
                .isEqualTo("user@gmail.com");

        assertThat(savedUser.getPassword())
                .isEqualTo("encodedPassword");

        assertThat(savedUser.getCode1C())
                .isEqualTo("C001");

        assertThat(savedUser.getName1C())
                .isEqualTo("John");

        assertThat(savedUser.getRole())
                .isEqualTo(RoleEnum.CUSTOMER);
    }

    @Test
    void register_shouldUseCustomerRoleWhenRoleIsNull() {

        RegisterDto dto = RegisterDto.builder()
                .email("user@gmail.com")
                .password("password123")
                .code1C("C001")
                .name1C("John")
                .role(null)
                .build();

        when(usersRepository.existsByEmail("user@gmail.com"))
                .thenReturn(false);

        when(passwordEncoder.encode("password123"))
                .thenReturn("encodedPassword");

        when(jwtService.generateAccessToken(any(Users.class)))
                .thenReturn("access");

        when(jwtService.generateRefreshToken(any(Users.class)))
                .thenReturn("refresh");

        usersService.register(dto);

        ArgumentCaptor<Users> captor =
                ArgumentCaptor.forClass(Users.class);

        verify(usersRepository)
                .save(captor.capture());

        assertThat(captor.getValue().getRole())
                .isEqualTo(RoleEnum.CUSTOMER);
    }

    @Test
    void register_shouldThrowExceptionWhenEmailAlreadyExists() {

        RegisterDto dto = RegisterDto.builder()
                .email("existing@gmail.com")
                .password("password")
                .build();

        when(usersRepository.existsByEmail("existing@gmail.com"))
                .thenReturn(true);

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> usersService.register(dto)
                );

        assertThat(exception.getMessage())
                .contains("User with email already exists");

        verify(usersRepository, never())
                .save(any());

        verify(passwordEncoder, never())
                .encode(any());
    }

    @Test
    void login_shouldReturnTokensForValidCredentials() {

        LoginDto dto = LoginDto.builder()
                .email("user@gmail.com")
                .password("password123")
                .build();

        Users user = Users.builder()
                .id(1L)
                .email("user@gmail.com")
                .password("encodedPassword")
                .role(RoleEnum.CUSTOMER)
                .build();

        when(usersRepository.findByEmail("user@gmail.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "password123",
                "encodedPassword"
        )).thenReturn(true);

        when(jwtService.generateAccessToken(user))
                .thenReturn("access-token");

        when(jwtService.generateRefreshToken(user))
                .thenReturn("refresh-token");

        Map<String, String> result =
                usersService.login(dto);

        assertThat(result)
                .containsEntry("access", "access-token")
                .containsEntry("refresh", "refresh-token");
    }

    @Test
    void login_shouldThrowExceptionWhenUserNotFound() {

        LoginDto dto = LoginDto.builder()
                .email("unknown@gmail.com")
                .password("password")
                .build();

        when(usersRepository.findByEmail("unknown@gmail.com"))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> usersService.login(dto)
                );

        assertThat(exception.getMessage())
                .isEqualTo("User not found");

        verify(passwordEncoder, never())
                .matches(anyString(), anyString());
    }

    @Test
    void login_shouldThrowExceptionWhenPasswordIsWrong() {

        LoginDto dto = LoginDto.builder()
                .email("user@gmail.com")
                .password("wrongPassword")
                .build();

        Users user = Users.builder()
                .email("user@gmail.com")
                .password("encodedPassword")
                .build();

        when(usersRepository.findByEmail("user@gmail.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "wrongPassword",
                "encodedPassword"
        )).thenReturn(false);

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> usersService.login(dto)
                );

        assertThat(exception.getMessage())
                .isEqualTo("Bad credentials");

        verify(jwtService, never())
                .generateAccessToken(any());
    }

    @Test
    void refresh_shouldReturnNewAccessToken() {

        String refreshToken = "refresh-token";

        Users user = Users.builder()
                .id(1L)
                .email("user@gmail.com")
                .build();

        when(jwtService.extractUsername(refreshToken))
                .thenReturn("user@gmail.com");

        when(usersRepository.findByEmail("user@gmail.com"))
                .thenReturn(Optional.of(user));

        when(jwtService.isValid(refreshToken, user))
                .thenReturn(true);

        when(jwtService.generateAccessToken(user))
                .thenReturn("new-access-token");

        Map<String, String> result =
                usersService.refresh(refreshToken);

        assertThat(result.get("access"))
                .isEqualTo("new-access-token");
    }

    @Test
    void refresh_shouldThrowExceptionWhenTokenIsInvalid() {

        String refreshToken = "invalid-token";

        Users user = Users.builder()
                .id(1L)
                .email("user@gmail.com")
                .build();

        when(jwtService.extractUsername(refreshToken))
                .thenReturn("user@gmail.com");

        when(usersRepository.findByEmail("user@gmail.com"))
                .thenReturn(Optional.of(user));

        when(jwtService.isValid(refreshToken, user))
                .thenReturn(false);

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> usersService.refresh(refreshToken)
                );

        assertThat(exception.getMessage())
                .isEqualTo("Invalid refresh token");
    }

    @Test
    void getByEmail_shouldReturnUser() {

        Users user = Users.builder()
                .id(1L)
                .email("user@gmail.com")
                .build();

        when(usersRepository.findByEmail("user@gmail.com"))
                .thenReturn(Optional.of(user));

        Optional<Users> result =
                usersService.getByEmail("user@gmail.com");

        assertThat(result)
                .isPresent();

        assertThat(result.get())
                .isEqualTo(user);

        verify(usersRepository)
                .findByEmail("user@gmail.com");
    }

    @Test
    void getMe_shouldReturnUserFromBearerToken() {

        String token = "jwt-token";

        Users user = Users.builder()
                .id(1L)
                .email("user@gmail.com")
                .build();

        when(jwtService.extractUsername(token))
                .thenReturn("user@gmail.com");

        when(usersRepository.findByEmail("user@gmail.com"))
                .thenReturn(Optional.of(user));

        Optional<Users> result =
                usersService.getMe("Bearer " + token);

        assertThat(result)
                .isPresent();

        assertThat(result.get())
                .isEqualTo(user);
    }

    @Test
    void getMe_shouldThrowExceptionWhenAuthorizationHeaderMissing() {

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> usersService.getMe(null)
                );

        assertThat(exception.getMessage())
                .isEqualTo("Missing Authorization header");

        verifyNoInteractions(jwtService);
        verifyNoInteractions(usersRepository);
    }

    @Test
    void getMe_shouldThrowExceptionWhenAuthorizationHeaderIsInvalid() {

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> usersService.getMe("Basic abc")
                );

        assertThat(exception.getMessage())
                .isEqualTo("Missing Authorization header");

        verifyNoInteractions(jwtService);
    }

    @Test
    void getMe_shouldThrowExceptionWhenUserDoesNotExist() {

        String token = "jwt-token";

        when(jwtService.extractUsername(token))
                .thenReturn("unknown@gmail.com");

        when(usersRepository.findByEmail("unknown@gmail.com"))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> usersService.getMe("Bearer " + token)
        );
    }
}
