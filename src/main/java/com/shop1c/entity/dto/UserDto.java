package com.shop1c.entity.dto;


import com.shop1c.entity.Users;
import com.shop1c.model.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;
    private String email;
    private String code1C;
    private String name1C;
    private RoleEnum role;

    public static UserDto fromEntity(Users user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setCode1C(user.getCode1C());
        dto.setName1C(user.getName1C());

        return dto;
    }
}
