package com.emara.SpringHotel.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequestDTO {
    @NotBlank(message = "email is required!")
    private String email;

    @NotBlank(message = "password is required!")
    private String password;



}
