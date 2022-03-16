package com.g.model;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;

public enum  AuthMethodEnum {
    KEY_PATH("KEY_PATH"),
    KEY_RAW("KEY_RAW"),
    USER_PASSWORD("USER_PASSWORD");

    private String code;
    AuthMethodEnum(String code) {
        this.code = code;
    }

    public static AuthMethodEnum get(String code) {
        Optional<AuthMethodEnum> first = Arrays.stream(values())
                .filter(item -> StringUtils.equalsIgnoreCase(item.getCode(), code))
                .findFirst();
        return first.orElse(null);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
