package com.mani.HotelBookingApp.ControllerTest;

import com.mani.HotelBookingApp.Controller.AdminController.ReservationController;
import com.mani.HotelBookingApp.DTO.ReservationDto;
import com.mani.HotelBookingApp.DTO.ReservationResponseDto;
import com.mani.HotelBookingApp.Exceptions.IllegalArgumentException;
import com.mani.HotelBookingApp.Service.Admin.Reservation.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

 class ReservationControllerTest {
    @InjectMocks
    private ReservationController reservationController;
    @Mock
    private ReservationService reservationService;
    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }
    @Test
     void testGetAllReservation_success() throws IllegalArgumentException {
        int pageNo=0;
        ReservationResponseDto reservationResponseDto= new ReservationResponseDto();
        reservationResponseDto.setReservationDto(List.of(
                new ReservationDto("2024-09-26", "2024-09-30", 2000, "APPROVED", 6, "Small Room", "Room No6", 3, "mani@gmail.com"),
                new ReservationDto("2024-09-27", "2024-09-29", 2000, "PENDING", 6, "Small Room", "Room No6", 3, "mani@gmail.com")));
        reservationResponseDto.setPageNo(pageNo);
        reservationResponseDto.setTotalPages(1);
        when(reservationService.getAllReservation(pageNo)).thenReturn(reservationResponseDto);
        ResponseEntity<?> response=reservationController.getAllReservation(pageNo);
        assertEquals(200, response.getStatusCodeValue());//NOSONAR
        assertEquals(reservationResponseDto,response.getBody());
        verify(reservationService,times(1)).getAllReservation(pageNo);
    }
    @Test
     void testGetAllReservation_withInvalidPageNo(){
        int invalidPageNo=-1;
        Exception exception=assertThrows(IllegalArgumentException.class,()->{
            reservationController.getAllReservation(invalidPageNo);
        });
        assertEquals("Invalid status or reservation ID",exception.getMessage());
        verify(reservationService,times(0)).getAllReservation(invalidPageNo);
    }
    @Test
     void testGetAllReservation_exceptionHandling(){
        int pageNo =0;
        when(reservationService.getAllReservation(pageNo)).thenThrow(new RuntimeException("Service error"));
        Exception exception=assertThrows(IllegalArgumentException.class,()->{
            reservationController.getAllReservation(pageNo);
        });
        assertEquals("Invalid status or reservation ID",exception.getMessage());
        verify(reservationService,times(1)).getAllReservation(pageNo);
    }
    @Test
     void testChangeReservationStatus_Success() throws IllegalArgumentException {
        Long reservationId=0L;
        String status="approved";
        when(reservationService.changeReservationStatus(reservationId,status)).thenReturn(true);
        ResponseEntity<?> response=reservationController.changeReservationStatus(reservationId,status);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        verify(reservationService,times(1)).changeReservationStatus(reservationId,status);
    }
    @Test
     void testChangeReservationStatus_invalid() {
        Long reservationId = 0L;
        String status = "INVALID";
        when(reservationService.changeReservationStatus(reservationId, status)).thenReturn(false);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            reservationController.changeReservationStatus(reservationId, status);
        });
        assertEquals("Invalid reservation ID", exception.getMessage());
        verify(reservationService, times(1)).changeReservationStatus(reservationId, status);
    }
}
