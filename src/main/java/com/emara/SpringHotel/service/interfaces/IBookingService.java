package com.emara.SpringHotel.service.interfaces;

import com.emara.SpringHotel.dto.ResponseDTO;
import com.emara.SpringHotel.entity.Booking;

public interface IBookingService {
    ResponseDTO saveBooking(Long roomId, Long userId, Booking bookingRequest);

    ResponseDTO findBookingByConfirmationCode(String confirmationCode);

    ResponseDTO getAllBookings();

    ResponseDTO cancelBooking(Long bookingId);
}
