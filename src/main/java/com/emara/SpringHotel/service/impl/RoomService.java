package com.emara.SpringHotel.service.impl;


import com.emara.SpringHotel.dto.ResponseDTO;
import com.emara.SpringHotel.dto.RoomDTO;
import com.emara.SpringHotel.entity.Room;
import com.emara.SpringHotel.exceptions.CustomException;
import com.emara.SpringHotel.repositories.RoomRepository;
import com.emara.SpringHotel.service.AwsS3Service;
import com.emara.SpringHotel.service.interfaces.IRoomService;
import com.emara.SpringHotel.utils.Utils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.List;

@Service
public class RoomService implements IRoomService {

    private final AwsS3Service awsS3Service;
    private final RoomRepository roomRepository;

    public RoomService(AwsS3Service awsS3Service, RoomRepository roomRepository) {
        this.awsS3Service = awsS3Service;
        this.roomRepository = roomRepository;
    }

    @Override
    public ResponseDTO addNewRoom(MultipartFile image, String roomType, BigDecimal roomPrice, String description) {
        ResponseDTO response = new ResponseDTO();

        try {
            String imageURL = awsS3Service.saveImageToS3(image);
            Room room = new Room();
            room.setRoomType(roomType);
            room.setRoomPrice(roomPrice);
            room.setRoomDescription(description);

            Room savedRoom = roomRepository.save(room);
            RoomDTO roomDTO = Utils.mapRoomEntityToRoomDTO(room);

            response.setStatusCode(200);
            response.setMessage("Successfully added new room");
            response.setRoom(roomDTO);
        } catch (CustomException ex) {
            response.setStatusCode(400);
            response.setMessage(ex.getMessage());
        } catch (Exception ex) {
            response.setStatusCode(500);
            response.setMessage("Something went wrong: " + ex.getMessage());
        }

        return response;
    }

    @Override
    public List<String> getAllRoomTypes() {
        return roomRepository.findDistinctRoomTypes();
    }

    @Override
    public ResponseDTO getAllRooms() {
        ResponseDTO response = new ResponseDTO();

        try {
            List<Room> roomList = roomRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<RoomDTO> roomDTOList = Utils.mapRoomListEntityToRoomListDTO(roomList);

            response.setStatusCode(200);
            response.setMessage("Successfully retrieved rooms");
            response.setRoomList(roomDTOList);
        } catch (CustomException ex) {
            response.setStatusCode(400);
            response.setMessage(ex.getMessage());
        } catch (Exception ex) {
            response.setStatusCode(500);
            response.setMessage("Something went wrong: " + ex.getMessage());
        }

        return response;
    }

    @Override
    public ResponseDTO deleteRoom(Long roomId) {
        ResponseDTO response = new ResponseDTO();

        try {
            roomRepository.findById(roomId).orElseThrow(() -> new CustomException("Room not found"));
            roomRepository.deleteById(roomId);
            response.setStatusCode(200);
            response.setMessage("Successfully deleted room");
        } catch (CustomException ex) {
            response.setStatusCode(400);
            response.setMessage(ex.getMessage());
        } catch (Exception ex) {
            response.setStatusCode(500);
            response.setMessage("Something went wrong: " + ex.getMessage());
        }

        return response;
    }

    @Override
    public ResponseDTO updateRoom(Long roomId, MultipartFile image, String roomType, BigDecimal roomPrice, String description) {
        ResponseDTO response = new ResponseDTO();

        try {
            String imageURL = null;
            if(image != null) {
                imageURL = awsS3Service.saveImageToS3(image);
            }
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new CustomException("Room not found"));
            if(roomType != null)
                room.setRoomType(roomType);
            if(roomPrice != null)
                room.setRoomPrice(roomPrice);
            if(description != null)
                room.setRoomDescription(description);
            if(imageURL != null)
                room.setRoomPhotoUrl(imageURL);

            Room updatedRoom = roomRepository.save(room);
            RoomDTO roomDTO = Utils.mapRoomEntityToRoomDTO(updatedRoom);

            response.setStatusCode(200);
            response.setMessage("Successfully updated room");
            response.setRoom(roomDTO);
        } catch (CustomException ex) {
            response.setStatusCode(400);
            response.setMessage(ex.getMessage());
        } catch (Exception ex) {
            response.setStatusCode(500);
            response.setMessage("Something went wrong: " + ex.getMessage());
        }


        return response;
    }

    @Override
    public ResponseDTO getRoomById(Long roomId) {
        ResponseDTO response = new ResponseDTO();

        try {
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new CustomException("Room not found"));
            RoomDTO roomDTO = Utils.mapRoomEntityToRoomDTO(room);

            response.setStatusCode(200);
            response.setMessage("Successfully get room");
            response.setRoom(roomDTO);
        } catch (CustomException ex) {
            response.setStatusCode(400);
            response.setMessage(ex.getMessage());
        } catch (Exception ex) {
            response.setStatusCode(500);
            response.setMessage("Something went wrong: " + ex.getMessage());
        }

        return response;
    }

    @Override
    public ResponseDTO getAvailableRoomsByDateAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
        ResponseDTO response = new ResponseDTO();

        try {
            List<Room> availableRooms = roomRepository.findAvailableRoomsByDateAndTypes(checkInDate, checkOutDate, roomType);
            List<RoomDTO> roomDTOList = Utils.mapRoomListEntityToRoomListDTO(availableRooms);

            response.setStatusCode(200);
            response.setMessage("Successfully get available rooms");
            response.setRoomList(roomDTOList);
        } catch (CustomException ex) {
            response.setStatusCode(400);
            response.setMessage(ex.getMessage());
        } catch (Exception ex) {
            response.setStatusCode(500);
            response.setMessage("Something went wrong: " + ex.getMessage());
        }

        return response;
    }

    @Override
    public ResponseDTO getAvailableRooms() {
        ResponseDTO response = new ResponseDTO();

        try {
            List<Room> availableRooms = roomRepository.findAvailableRooms();
            List<RoomDTO> roomDTOList = Utils.mapRoomListEntityToRoomListDTO(availableRooms);

            response.setStatusCode(200);
            response.setMessage("Successfully get available rooms");
            response.setRoomList(roomDTOList);
        } catch (CustomException ex) {
            response.setStatusCode(400);
            response.setMessage(ex.getMessage());
        } catch (Exception ex) {
            response.setStatusCode(500);
            response.setMessage("Something went wrong: " + ex.getMessage());
        }

        return response;
    }
}
