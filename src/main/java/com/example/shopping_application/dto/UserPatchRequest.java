package com.example.shopping_application.dto;

import lombok.Data;

@Data
public class UserPatchRequest {
    private String name;
    private String email;
    private String mobile;
    private String address;
    private String gender;
}

