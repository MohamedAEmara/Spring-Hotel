package com.emara.SpringHotel.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)  // Don't show data that have null values.
public class UserDTO {
//    We use UserDTO to get user data, so we don't include "password" field
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String role;
    private List<BookingDTO> bookings;
}
