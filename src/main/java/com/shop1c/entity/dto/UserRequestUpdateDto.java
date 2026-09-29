package com.shop1c.entity.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestUpdateDto {

    private String code1C;
    private String name1C;
    private String email;
    private String role;
}
