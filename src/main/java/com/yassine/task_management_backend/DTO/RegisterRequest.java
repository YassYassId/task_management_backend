package com.yassine.task_management_backend.DTO;


import lombok.Data;

@Data
public class RegisterRequest {
    private String nom;
    private String prenom;
    private String email;
    private String password;
}
