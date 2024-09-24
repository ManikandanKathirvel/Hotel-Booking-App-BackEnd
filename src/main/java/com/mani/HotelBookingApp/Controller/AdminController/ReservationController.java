package com.mani.HotelBookingApp.Controller.AdminController;//NOSONAR

import com.mani.HotelBookingApp.Service.Admin.Reservation.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/admin")
@CrossOrigin
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }
    @GetMapping("/reservation/{pageNo}")
    public ResponseEntity<?> getAllReservation(@PathVariable int pageNo){
        try{
            return ResponseEntity.ok(reservationService.getALlReservation(pageNo));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("SOmething went worng");
        }
    }

    @GetMapping("/res/{id}/{status}")
    public ResponseEntity<?> changeReservationStatus(@PathVariable Long id,@PathVariable String status){
    boolean success= reservationService.changeReservationStatus(id,status);
    if(success){
        return ResponseEntity.ok().build();
    }
    else {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went Wrong");
    }
    }
}
