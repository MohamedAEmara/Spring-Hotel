package com.emara.SpringHotel.service.interfaces;

import com.emara.SpringHotel.dto.ResponseDTO;
import com.emara.SpringHotel.dto.RoomDTO;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.math.BigDecimal;

public interface IRoomService {
    ResponseDTO addNewRoom(MultipartFile image, String roomType, BigDecimal roomPrice, String description);

    ResponseDTO getAllRoomTypes();

    ResponseDTO getAllRooms();

    ResponseDTO deleteRoom(Long roomId);

    ResponseDTO updateRoom(Long roomId, MultipartFile image, String roomType, BigDecimal roomPrice, String description);

    ResponseDTO getRoomById(Long roomId);

    ResponseDTO getAvailableRoomsByDateAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType);

    ResponseDTO getAvailableRooms();
}
