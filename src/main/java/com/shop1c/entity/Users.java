package com.shop1c.entity;

import jakarta.persistence.*;
import lombok.*;
import com.shop1c.model.RoleEnum;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    @Email(message = "*Please provide a valid Email")
    @NotEmpty(message = "*Please provide an email")
    private String email;

    @Column(name = "password")
    @Length(min = 5, message = "*Your password must have at least 5 characters")
    @NotEmpty(message = "*Please provide your password")
    private String password;

    @Column(name = "code1c")
    @NotEmpty(message = "*Please provide your code 1C")
    private String code1C;

    @Column(name = "name1c")
    @NotEmpty(message = "*Please provide your name 1C")
    private String name1C;

    @Column(name = "active")
    private boolean active;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private RoleEnum role;


}
