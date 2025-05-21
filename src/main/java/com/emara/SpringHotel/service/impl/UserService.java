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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        ResponseDTO response = new ResponseDTO();

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            var user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(()-> new CustomException("User Not Found!"));
            var token = jwtUtils.generateToken(user);

            response.setStatusCode(200);
            response.setToken(token);
            response.setRole(user.getRole());
            response.setExpirationTime("7 Days");
            response.setMessage("User logged in successfully!");
        } catch (CustomException ex) {
            response.setStatusCode(404);
            response.setMessage(ex.getMessage());
        } catch (Exception ex) {
            response.setStatusCode(500);
            response.setMessage("Something went wrong: \n" + ex.getMessage());
        }

        return response;
    }

    @Override
    public ResponseDTO getUserById(Long userId) {
        ResponseDTO response = new ResponseDTO();

        try {
            User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new CustomException("User Not Found!"));
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(user);

            response.setStatusCode(200);
            response.setMessage("User data returned successfully!");
            response.setUser(userDTO);
        } catch (CustomException ex) {
            response.setStatusCode(404);
            response.setMessage(ex.getMessage());
        } catch (Exception ex) {
            response.setStatusCode(500);
            response.setMessage(ex.getMessage());
        }

        return response;
    }

    @Override
    public ResponseDTO getAllUsers() {
        ResponseDTO response = new ResponseDTO();
        try {
            List<User> userList = userRepository.findAll();
            List<UserDTO> userDTOList = new ArrayList<>();
            userDTOList = Utils.mapUserListEntityToUserListDTO(userList);

            response.setStatusCode(200);
            response.setMessage("All users found!");
            response.setUserList(userDTOList);
        } catch (CustomException ex) {
            response.setStatusCode(404);
            response.setMessage(ex.getMessage());
        } catch (Exception ex) {
            response.setStatusCode(500);
            response.setMessage("Something went wrong: \n" + ex.getMessage());
        }

        return response;
    }

    @Override
    public ResponseDTO getUserBookingHistory(String userId) {
        ResponseDTO response = new ResponseDTO();

        try {
            User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new CustomException("User Not Found!"));
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(user);

            response.setStatusCode(200);
            response.setMessage("User booking history successfully!");
            response.setUser(userDTO);
        } catch (CustomException ex) {
            response.setStatusCode(404);
            response.setMessage(ex.getMessage());
        } catch (Exception ex) {
            response.setStatusCode(500);
            response.setMessage(ex.getMessage());
        }

        return response;
    }

    @Override
    public ResponseDTO deleteUser(Long userId) {
        ResponseDTO response = new ResponseDTO();

        try {
            // We use this line before deletion to throw an error if the user_id is not correct.
            userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new CustomException("User Not Found!"));
            userRepository.deleteById(Long.valueOf(userId));

            response.setStatusCode(200);
            response.setMessage("User deleted successfully!");
        } catch (CustomException ex) {
            response.setStatusCode(404);
            response.setMessage(ex.getMessage());
        } catch (Exception ex) {
            response.setStatusCode(500);
            response.setMessage(ex.getMessage());
        }

        return response;
    }

    @Override
    public ResponseDTO getMyInfo(String email) {
        ResponseDTO response = new ResponseDTO();

        try {
            User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomException("User Not Found!"));
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(user);

            response.setStatusCode(200);
            response.setMessage("User info returned successfully!");
            response.setUser(userDTO);
        } catch (CustomException ex) {
            response.setStatusCode(404);
            response.setMessage(ex.getMessage());
        } catch (Exception ex) {
            response.setStatusCode(500);
            response.setMessage(ex.getMessage());
        }

        return response;
    }
}
