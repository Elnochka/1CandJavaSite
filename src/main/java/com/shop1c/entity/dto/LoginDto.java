package com.shop1c.entity.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class LoginDto {

    private String email;
    private String password;
}
