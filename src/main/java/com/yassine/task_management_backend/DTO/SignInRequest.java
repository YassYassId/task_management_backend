package com.yassine.task_management_backend.DTO;

import lombok.Data;

@Data
public class SignInRequest {
    private String email;
    private String password;
}
