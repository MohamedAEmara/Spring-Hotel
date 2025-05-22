package com.emara.SpringHotel.controller;

import com.emara.SpringHotel.dto.ResponseDTO;
import com.emara.SpringHotel.service.interfaces.IRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/rooms")
public class RoomController {
    @Autowired
    private IRoomService roomService;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseDTO> addNewRoom(
            @RequestParam(value = "image")MultipartFile image,
            @RequestParam(value = "roomType")String roomType,
            @RequestParam(value = "roomPrice") BigDecimal roomPrice,
            @RequestParam(value = "roomDescription")String roomDescription
    ) {
        // Validate existence of required fields (image, type, price)
        if(image == null || image.isEmpty() || roomType == null || roomType.isBlank() || roomPrice == null) {
            ResponseDTO response = new ResponseDTO();
            response.setStatusCode(400);
            response.setMessage("Please fill all the required fields (image, roomType, roomPrice, roomDescription)");
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }

        ResponseDTO response = roomService.addNewRoom(image, roomType, roomPrice, roomDescription);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseDTO> getAllRooms() {
        ResponseDTO response = roomService.getAllRooms();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/types")
    public ResponseEntity<ResponseDTO> getAllRoomTypes() {
        ResponseDTO response = roomService.getAllRoomTypes();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/available")
    public ResponseEntity<ResponseDTO> getAllAvailableRooms() {
        ResponseDTO response = roomService.getAvailableRooms();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/available/date-type")
    public ResponseEntity<ResponseDTO> getAllAvailableRoomByDateAndType(
            @RequestParam() @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate  checkInDate,
            @RequestParam() @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
            @RequestParam()String roomType
    ) {
        // Validate RequestParams
        if(checkInDate == null || checkOutDate == null || roomType == null || roomType.isBlank()) {
            ResponseDTO response = new ResponseDTO();
            response.setStatusCode(400);
            response.setMessage("Please fill all the required fields (checkInDate, checkOutDate, roomType)");
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }
        ResponseDTO response = roomService.getAvailableRoomsByDateAndType(checkInDate, checkOutDate, roomType);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<ResponseDTO> getUserByRoomId(@PathVariable Long roomId) {
        ResponseDTO response = roomService.getRoomById(roomId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @PutMapping("/{roomId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseDTO> updateRoom(
            @PathVariable Long roomId,
            @RequestParam(value = "image")MultipartFile image,
            @RequestParam(value = "roomType")String roomType,
            @RequestParam(value = "roomPrice") BigDecimal roomPrice,
            @RequestParam(value = "roomDescription")String roomDescription
    ) {
        ResponseDTO response = roomService.updateRoom(roomId, image, roomType, roomPrice, roomDescription);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @DeleteMapping("/{roomId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseDTO> deleteRoom(@PathVariable Long roomId) {
        ResponseDTO response = roomService.deleteRoom(roomId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

}
