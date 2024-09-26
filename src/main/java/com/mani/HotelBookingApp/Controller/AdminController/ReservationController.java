package com.mani.HotelBookingApp.Controller.AdminController;//NOSONAR

import com.mani.HotelBookingApp.DTO.ReservationResponseDto;
import com.mani.HotelBookingApp.Exceptions.IllegalArgumentException;
import com.mani.HotelBookingApp.Service.Admin.Reservation.ReservationService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin("http://localhost:4200")
public class ReservationController {
    private final ReservationService reservationService;
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }
    @GetMapping("/reservation/{pageNo}")
    public ResponseEntity<?> getAllReservation(@PathVariable int pageNo) throws IllegalArgumentException {
        if (pageNo < 0) {
            throw new IllegalArgumentException("Invalid status or reservation ID");
        }
        try {
            ReservationResponseDto reservationResponseDto=reservationService.getAllReservation(pageNo);
            return ResponseEntity.ok(reservationResponseDto);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid status or reservation ID");
        }
    }
    @GetMapping("/res/{id}/{status}")
    public ResponseEntity<?> changeReservationStatus(@PathVariable Long id, @PathVariable String status) throws IllegalArgumentException {
        boolean success = reservationService.changeReservationStatus(id, status);
        if (success) {
            return ResponseEntity.ok().build();
        } else {
            throw new IllegalArgumentException("Invalid reservation ID");
            //return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid reservation ID");
        }
    }
}
