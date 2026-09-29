package com.shop1c.service.impl;

import com.shop1c.entity.Users;
import com.shop1c.entity.dto.UserDto;
import com.shop1c.entity.dto.UserRequestUpdateDto;
import com.shop1c.model.RoleEnum;
import com.shop1c.repository.UsersRepository;
import com.shop1c.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UsersRepository usersRepository;

    @Override
    public List<UserDto> getAllUsers() {

        log.debug("Loading all users. ");

        List<UserDto> result = usersRepository.findAll()
                .stream()
                .map(user -> new UserDto(
                        user.getId(),
                        user.getEmail(),
                        user.getCode1C(),
                        user.getName1C(),
                        user.getRole()
                ))
                .toList();

        log.info("Loaded {} users", result.size());

        return result;

    }

    @Override
    public Users updateUser(Long id, UserRequestUpdateDto request) {
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Користувача не знайдено"
                ));

        user.setCode1C(request.getCode1C());
        user.setName1C(request.getName1C());
        user.setEmail(request.getEmail());

        if (request.getRole() != null) {
            user.setRole(RoleEnum.valueOf(request.getRole()));
        }

        log.info("Updated user successfully. ");

        return usersRepository.save(user);
    }
}
