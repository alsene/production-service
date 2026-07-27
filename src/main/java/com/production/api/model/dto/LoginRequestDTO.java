package com.production.api.model.dto;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String email;
    private String password;
}

