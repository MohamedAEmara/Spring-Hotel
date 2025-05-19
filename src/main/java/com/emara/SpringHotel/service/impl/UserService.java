package com.emara.SpringHotel.service.impl;

import com.emara.SpringHotel.dto.LoginRequestDTO;
import com.emara.SpringHotel.dto.ResponseDTO;
import com.emara.SpringHotel.dto.UserDTO;
import com.emara.SpringHotel.entity.User;
import com.emara.SpringHotel.exceptions.CustomException;
import com.emara.SpringHotel.repositories.UserRepository;
import com.emara.SpringHotel.service.interfaces.IUserService;
import com.emara.SpringHotel.utils.JWTUtils;
import com.emara.SpringHotel.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService implements IUserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public ResponseDTO signupUser(User user) {
        ResponseDTO response = new ResponseDTO();
        try {
            if(user.getRole() == null || user.getRole().isBlank()) {
                user.setRole("USER");
            }
            if(userRepository.existsByEmail(user.getEmail())) {
                throw new CustomException(user.getEmail() + " already exists!");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userRepository.save(user);
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(savedUser);
            response.setStatusCode(200);
            response.setMessage("User registered successfully!");
            response.setUser(userDTO);

        } catch (CustomException ex) {
            response.setStatusCode(400);
            response.setMessage(ex.getMessage());
        }
        catch (Exception ex) {
            response.setStatusCode(500);
            response.setMessage("Something went wrong: \n" + ex.getMessage());
        }
        return response;
    }

    @Override
    public ResponseDTO loginUser(LoginRequestDTO loginRequest) {
        return null;
    }

    @Override
    public ResponseDTO getUserById(String userId) {
        return null;
    }

    @Override
    public ResponseDTO getAllUsers() {
        return null;
    }

    @Override
    public ResponseDTO getUserBookingHistory(String userId) {
        return null;
    }

    @Override
    public ResponseDTO deleteUser(String userId) {
        return null;
    }

    @Override
    public ResponseDTO getMyInfo(String userId) {
        return null;
    }
}
