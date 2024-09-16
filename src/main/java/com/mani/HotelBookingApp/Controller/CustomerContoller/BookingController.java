package com.mani.HotelBookingApp.Controller.CustomerContoller;

import com.mani.HotelBookingApp.DTO.ReservationDto;
import com.mani.HotelBookingApp.Service.Customer.Booking.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
@CrossOrigin("http://localhost:4200")

public class BookingController {
    @Autowired
    BookingService bookingService;

    @PostMapping("/booking")
    public ResponseEntity<?> bookingRoom(@RequestBody ReservationDto reservationDto) {
        System.out.println(reservationDto.getUserId());

        boolean success = bookingService.postReservation(reservationDto);
        if (success) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
