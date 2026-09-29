package com.shop1c.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum RoleEnum {

    CUSTOMER,
    ADMIN;

    @JsonCreator
    public static RoleEnum fromString(String value) {
        if (value == null) return null;
        return RoleEnum.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
