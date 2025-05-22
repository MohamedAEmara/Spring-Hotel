package com.emara.SpringHotel.service.impl;


import com.emara.SpringHotel.dto.BookingDTO;
import com.emara.SpringHotel.dto.ResponseDTO;
import com.emara.SpringHotel.entity.Booking;
import com.emara.SpringHotel.entity.Room;
import com.emara.SpringHotel.entity.User;
import com.emara.SpringHotel.exceptions.CustomException;
import com.emara.SpringHotel.repositories.BookingRepository;
import com.emara.SpringHotel.repositories.RoomRepository;
import com.emara.SpringHotel.repositories.UserRepository;
import com.emara.SpringHotel.service.interfaces.IBookingService;
import com.emara.SpringHotel.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService implements IBookingService {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private UserRepository userRepository;


    @Override
    public ResponseDTO saveBooking(Long roomId, Long userId, Booking bookingRequest) {
        ResponseDTO response = new ResponseDTO();

        try {
            if (bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())) {
                throw new IllegalArgumentException("Check out date must come after check out date");
            }
            // Validate roomId & userId
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new CustomException("Room not found"));
            User user = userRepository.findById(userId).orElseThrow(() -> new CustomException("User not found"));

            // Validate valid check-in & check-out
            List<Booking> existingBookings = room.getBookings();
            if (!roomIsAvailable(bookingRequest, existingBookings)) {
                throw new CustomException("Room is not available for selected date range!");
            }

            bookingRequest.setRoom(room);
            bookingRequest.setUser(user);
            String bookingConfirmationCode = Utils.generateRandomAlphanumeric(10);
            bookingRequest.setBookingConfirmationCode(bookingConfirmationCode);
            bookingRepository.save(bookingRequest);

            response.setStatusCode(200);
            response.setMessage("Booking successfully saved");
            response.setBookingConfirmationCode(bookingConfirmationCode);

        } catch (CustomException ex) {
            response.setStatusCode(404);
            response.setMessage(ex.getMessage());
        } catch (Exception ex) {
            response.setStatusCode(500);
            response.setMessage("Something went wrong: " + ex.getMessage());
        }

        return response;
    }


    @Override
    public ResponseDTO findBookingByConfirmationCode(String confirmationCode) {
        ResponseDTO response = new ResponseDTO();

        try {
            Booking booking = bookingRepository.findByBookingConfirmationCode(confirmationCode).orElseThrow(() -> new CustomException("Booking not found"));
            BookingDTO bookingDTO = Utils.mapBookingEntityToBookingDTO(booking);

            response.setStatusCode(200);
            response.setMessage("Booking successfully found");
            response.setBooking(bookingDTO);
        } catch (CustomException ex) {
            response.setStatusCode(404);
            response.setMessage(ex.getMessage());
        } catch (Exception ex) {
            response.setStatusCode(500);
            response.setMessage("Something went wrong: " + ex.getMessage());
        }

        return response;
    }

    @Override
    public ResponseDTO getAllBookings() {
        ResponseDTO response = new ResponseDTO();

        try {
            List<Booking> bookings = bookingRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<BookingDTO> bookingDTOList = Utils.mapBookingListEntityToBookingListDTO(bookings);

            response.setStatusCode(200);
            response.setMessage("Bookings successfully found");
            response.setBookingList(bookingDTOList);
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
    public ResponseDTO cancelBooking(Long bookingId) {
        ResponseDTO response = new ResponseDTO();

        try {
            Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new CustomException("Booking not found"));
            bookingRepository.deleteById(bookingId);

            response.setStatusCode(200);
            response.setMessage("Booking successfully cancelled");

        } catch (CustomException ex) {
            response.setStatusCode(400);
            response.setMessage(ex.getMessage());
        } catch (Exception ex) {
            response.setStatusCode(500);
            response.setMessage("Something went wrong: " + ex.getMessage());
        }

        return response;
    }


    private boolean roomIsAvailable(Booking bookingRequest, List<Booking> existingBookings) {
        return existingBookings.stream()
                .noneMatch(existingBooking ->
                        bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate())
                        &&
                        bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckInDate())
                );
    }
}
















