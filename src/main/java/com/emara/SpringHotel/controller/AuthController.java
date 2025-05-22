package com.emara.SpringHotel.controller;

import com.emara.SpringHotel.dto.LoginRequestDTO;
import com.emara.SpringHotel.dto.ResponseDTO;
import com.emara.SpringHotel.entity.User;
import com.emara.SpringHotel.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private IUserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ResponseDTO> signup(@RequestBody User user) {
        ResponseDTO response = userService.signupUser(user);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        ResponseDTO response = userService.loginUser(loginRequestDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
