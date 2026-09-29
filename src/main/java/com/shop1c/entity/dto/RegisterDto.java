package com.shop1c.entity.dto;

import com.shop1c.model.RoleEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class RegisterDto {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 5, message = "Password must be at least 5 characters long")
    private String password;


    private String code1C;


    private String name1C;

    private RoleEnum role;
}
