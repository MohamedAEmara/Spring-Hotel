package com.emara.SpringHotel.controller;

import com.emara.SpringHotel.dto.ResponseDTO;
import com.emara.SpringHotel.entity.Booking;
import com.emara.SpringHotel.service.impl.RoomService;
import com.emara.SpringHotel.service.interfaces.IBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    @Autowired
    private IBookingService bookingService;
    private RoomService roomService;

    @PostMapping("/book/{roomId}/{userId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<ResponseDTO> book(
            @PathVariable Long roomId,
            @PathVariable Long userId,
            @RequestBody Booking bookingRequest
    ) {
        ResponseDTO response = bookingService.saveBooking(roomId, userId, bookingRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseDTO> getAllBookings() {
        ResponseDTO response = bookingService.getAllBookings();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @GetMapping("/confirmation-code/{confirmationCode}")
    public ResponseEntity<ResponseDTO> getBookingByConfirmationCode(@PathVariable String confirmationCode) {
        ResponseDTO response = bookingService.findBookingByConfirmationCode(confirmationCode);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @DeleteMapping("/{bookingId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<ResponseDTO> cancelBooking(@PathVariable Long bookingId) {
        ResponseDTO response = bookingService.cancelBooking(bookingId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
