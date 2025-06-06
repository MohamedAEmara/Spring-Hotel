package com.emara.SpringHotel.service.interfaces;

import com.emara.SpringHotel.dto.LoginRequestDTO;
import com.emara.SpringHotel.dto.ResponseDTO;
import com.emara.SpringHotel.entity.User;

public interface IUserService {
    ResponseDTO signupUser(User user);
    ResponseDTO loginUser(LoginRequestDTO loginRequest);
    ResponseDTO getUserById(Long userId);
    ResponseDTO getAllUsers();
    ResponseDTO getUserBookingHistory(String userId);
    ResponseDTO deleteUser(Long userId);
    ResponseDTO getMyInfo(String userId);
}
