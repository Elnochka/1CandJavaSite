package com.shop1c.service;

import com.shop1c.entity.Users;
import com.shop1c.entity.dto.UserDto;
import com.shop1c.entity.dto.UserRequestUpdateDto;
import com.shop1c.model.RoleEnum;
import com.shop1c.repository.UsersRepository;
import com.shop1c.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UsersRepository usersRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getAllUsers_shouldReturnAllUsers() {

        Users user1 = Users.builder()
                .id(1L)
                .email("user1@gmail.com")
                .code1C("C001")
                .name1C("User 1")
                .build();

        Users user2 = Users.builder()
                .id(2L)
                .email("user2@gmail.com")
                .code1C("C002")
                .name1C("User 2")
                .build();

        when(usersRepository.findAll())
                .thenReturn(List.of(user1, user2));

        List<UserDto> result =
                userService.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals(1L, result.get(0).getId());
        assertEquals("user1@gmail.com", result.get(0).getEmail());
        assertEquals("C001", result.get(0).getCode1C());
        assertEquals("User 1", result.get(0).getName1C());


        assertEquals(2L, result.get(1).getId());
        assertEquals("user2@gmail.com", result.get(1).getEmail());
        assertEquals("C002", result.get(1).getCode1C());
        assertEquals("User 2", result.get(1).getName1C());


        verify(usersRepository).findAll();

    }

    @Test
    void updateUser_shouldUpdateUser() {

        Long userId = 1L;

        Users user = Users.builder()
                .id(userId)
                .email("old@gmail.com")
                .code1C("OLD001")
                .name1C("Old Name")
                .role(RoleEnum.CUSTOMER)
                .build();

        UserRequestUpdateDto request =
                UserRequestUpdateDto.builder()
                        .code1C("NEW001")
                        .name1C("New Name")
                        .email("new@gmail.com")
                        .role("ADMIN")
                        .build();

        when(usersRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(usersRepository.save(user))
                .thenReturn(user);

        Users result =
                userService.updateUser(userId, request);

        assertThat(result.getCode1C())
                .isEqualTo("NEW001");

        assertThat(result.getName1C())
                .isEqualTo("New Name");

        assertThat(result.getEmail())
                .isEqualTo("new@gmail.com");

        assertThat(result.getRole())
                .isEqualTo(RoleEnum.ADMIN);

        verify(usersRepository)
                .findById(userId);

        verify(usersRepository)
                .save(user);
    }

    @Test
    void updateUser_shouldKeepRoleWhenRoleIsNull() {

        Long userId = 1L;

        Users user = Users.builder()
                .id(userId)
                .email("user@gmail.com")
                .code1C("C001")
                .name1C("User")
                .role(RoleEnum.CUSTOMER)
                .build();

        UserRequestUpdateDto request =
                UserRequestUpdateDto.builder()
                        .code1C("C002")
                        .name1C("Updated User")
                        .email("updated@gmail.com")
                        .role(null)
                        .build();

        when(usersRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(usersRepository.save(user))
                .thenReturn(user);

        Users result =
                userService.updateUser(userId, request);

        assertThat(result.getRole())
                .isEqualTo(RoleEnum.CUSTOMER);

        assertThat(result.getCode1C())
                .isEqualTo("C002");
    }

    @Test
    void updateUser_shouldThrowNotFoundWhenUserDoesNotExist() {

        Long userId = 999L;

        UserRequestUpdateDto request =
                UserRequestUpdateDto.builder()
                        .code1C("C001")
                        .name1C("User")
                        .email("user@gmail.com")
                        .build();

        when(usersRepository.findById(userId))
                .thenReturn(Optional.empty());

        ResponseStatusException exception =
                assertThrows(
                        ResponseStatusException.class,
                        () -> userService.updateUser(userId, request)
                );

        assertThat(exception.getStatusCode().value())
                .isEqualTo(404);

        verify(usersRepository, never())
                .save(any());
    }
}
