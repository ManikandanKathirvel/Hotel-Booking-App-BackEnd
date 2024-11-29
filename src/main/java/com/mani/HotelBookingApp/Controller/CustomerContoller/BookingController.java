package com.mani.HotelBookingApp.Controller.CustomerContoller;//NOSONAR

import com.mani.HotelBookingApp.DTO.ReservationDto;
import com.mani.HotelBookingApp.Exceptions.ResourceNotFoundException;
import com.mani.HotelBookingApp.Service.Customer.Booking.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
@CrossOrigin("http://localhost:4200")
public class BookingController {
    private final BookingService bookingService;
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }
    @PostMapping("/booking")
    public ResponseEntity<?> bookingRoom(@RequestBody ReservationDto reservationDto) {//NOSONAR
        System.out.println(reservationDto.getUserId());//NOSONAR
        boolean success = bookingService.postReservation(reservationDto);
        if (success) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        throw new IllegalArgumentException("Request Invalid");
    }
    @GetMapping("/bookings/{userId}/{pageNo}")
    public ResponseEntity<?> getAllBookingUserId(@PathVariable Long userId, @PathVariable int pageNo) throws ResourceNotFoundException {//NOSONAR
        try {
            return ResponseEntity.ok(bookingService.getAllReservationByUserId(userId, pageNo));
        } catch (Exception e) {
            throw new ResourceNotFoundException("Resource Not Found");
        }
    }
}