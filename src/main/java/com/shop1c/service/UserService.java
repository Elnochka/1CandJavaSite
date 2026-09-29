package com.shop1c.service;

import com.shop1c.entity.Users;
import com.shop1c.entity.dto.UserDto;
import com.shop1c.entity.dto.UserRequestUpdateDto;

import java.util.List;

public interface UserService {

    List<UserDto> getAllUsers();

    Users updateUser(Long id, UserRequestUpdateDto request);
}
