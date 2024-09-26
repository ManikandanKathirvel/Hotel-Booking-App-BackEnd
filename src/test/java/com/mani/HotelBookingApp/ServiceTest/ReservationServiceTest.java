package com.mani.HotelBookingApp.ServiceTest;

import com.mani.HotelBookingApp.DTO.ReservationResponseDto;
import com.mani.HotelBookingApp.Entity.Reservation;
import com.mani.HotelBookingApp.Entity.Room;
import com.mani.HotelBookingApp.Entity.User;
import com.mani.HotelBookingApp.Enum.ReservationStatus;
import com.mani.HotelBookingApp.Repository.ReservationRepo;
import com.mani.HotelBookingApp.Repository.RoomRepo;
import com.mani.HotelBookingApp.Service.Admin.Reservation.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReservationServiceTest {
    @InjectMocks
    private ReservationService reservationService;
    @Mock
    private ReservationRepo reservationRepo;
    @Mock
    private RoomRepo roomRepo;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllReservation_withValidPage() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 4);
        Reservation reservation1 = createMockReservation(1L, LocalDate.now(), LocalDate.now().plusDays(1), 200L);
        Reservation reservation2 = createMockReservation(2L, LocalDate.now(), LocalDate.now().plusDays(1), 300L);
        Reservation reservation3 = createMockReservation(3L, LocalDate.now(), LocalDate.now().plusDays(1), 400L);
        Reservation reservation4 = createMockReservation(4L, LocalDate.now(), LocalDate.now().plusDays(1), 500L);
        Page<Reservation> reservationPage = new PageImpl<>(Arrays.asList(reservation1, reservation2, reservation3, reservation4), pageable, 4);

        when(reservationRepo.findAll(pageable)).thenReturn(reservationPage);

        ReservationResponseDto response = reservationService.getAllReservation(0);
        assertNotNull(response);
        assertEquals(4, response.getReservationDto().size());
        assertEquals(0, response.getPageNo());
        assertEquals(1, response.getTotalPages()); // Assuming 1 page since total = 4 and page size = 4
        verify(reservationRepo, times(1)).findAll(pageable);
    }

    @Test
    void testGetAllReservation_withNoReservations() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 4);
        Page<Reservation> reservationPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(reservationRepo.findAll(pageable)).thenReturn(reservationPage);

        // Act
        ReservationResponseDto response = reservationService.getAllReservation(0);

        // Assert
        assertNotNull(response);
        assertEquals(0, response.getReservationDto().size());
        assertEquals(0, response.getPageNo());
        assertEquals(0, response.getTotalPages());
        verify(reservationRepo, times(1)).findAll(pageable);
    }
    @Test
    void testGetAllReservation_withPageExceedingTotalPages() {
        // Arrange
        Pageable pageable = PageRequest.of(5, 4); // Page 5, page size 4
        Page<Reservation> reservationPage = new PageImpl<>(Collections.emptyList(), pageable, 8); // Total 8 results

        when(reservationRepo.findAll(pageable)).thenReturn(reservationPage);

        // Act
        ReservationResponseDto response = reservationService.getAllReservation(5);

        // Assert
        assertNotNull(response);
        assertEquals(0, response.getReservationDto().size()); // No results on page 5
        assertEquals(5, response.getPageNo());
        assertEquals(2, response.getTotalPages()); // 8 results with page size of 4 means 2 total pages
        verify(reservationRepo, times(1)).findAll(pageable);
    }

    @Test
    void testGetAllReservation_withInvalidNegativePage() {
        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            reservationService.getAllReservation(-1);
        });
    }

    private Reservation createMockReservation(Long id, LocalDate checkIn, LocalDate checkOut, Long price) {
        Reservation reservation = new Reservation();
        reservation.setId(id);
        reservation.setCheckInDate(checkIn);
        reservation.setCheckOutDate(checkOut);
        reservation.setPrice(price);

        User user = new User();
        user.setId(1L);
        user.setUsername("mani@gmail.com");

        Room room=new Room();
        room.setId(2L);
        room.setName("Room NO3");
        room.setType("AC");
        reservation.setRoom(room);

        reservation.setUser(user);
        return reservation;
    }
    @Test
    void testChangeReservationStatus_withValidApproval() {
        // Arrange
        Long reservationId = 1L;
        Reservation reservation = createMockReservation(reservationId);
        when(reservationRepo.findById(reservationId)).thenReturn(Optional.of(reservation));

        // Act
        boolean result = reservationService.changeReservationStatus(reservationId, "Approve");

        // Assert
        assertTrue(result);
        assertEquals(ReservationStatus.APPROVED, reservation.getReservationStatus());
        verify(reservationRepo, times(1)).save(reservation);
        verify(roomRepo, times(1)).save(reservation.getRoom());
    }

    @Test
    void testChangeReservationStatus_withRejection() {
        // Arrange
        Long reservationId = 2L;
        Reservation reservation = createMockReservation(reservationId);
        when(reservationRepo.findById(reservationId)).thenReturn(Optional.of(reservation));

        // Act
        boolean result = reservationService.changeReservationStatus(reservationId, "Reject");

        // Assert
        assertTrue(result);
        assertEquals(ReservationStatus.REJECTED, reservation.getReservationStatus());
        verify(reservationRepo, times(1)).save(reservation);
        verify(roomRepo, times(1)).save(reservation.getRoom());
    }

    @Test
    void testChangeReservationStatus_withInvalidReservation() {
        Long reservationId = 3L;
        when(reservationRepo.findById(reservationId)).thenReturn(Optional.empty());

        boolean result = reservationService.changeReservationStatus(reservationId, "Approve");

        assertFalse(result);
        verify(reservationRepo, times(0)).save(any(Reservation.class));
        verify(roomRepo, times(0)).save(any(Room.class));
    }

    // Utility method to create a mock Reservation
    private Reservation createMockReservation(Long id) {
        Reservation reservation = new Reservation();
        reservation.setId(id);
        reservation.setReservationStatus(ReservationStatus.PENDING); // Initially pending

        Room room = new Room();
        room.setId(1L);
        room.setAvailable(true); // Initially available
        reservation.setRoom(room);

        return reservation;
    }

}
