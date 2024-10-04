package com.mani.HotelBookingApp.Service.Customer.Booking;//NOSONAR

import com.mani.HotelBookingApp.DTO.ReservationDto;
import com.mani.HotelBookingApp.DTO.ReservationResponseDto;
import com.mani.HotelBookingApp.Entity.Reservation;
import com.mani.HotelBookingApp.Entity.Room;
import com.mani.HotelBookingApp.Entity.User;
import com.mani.HotelBookingApp.Enum.ReservationStatus;
import com.mani.HotelBookingApp.Repository.ReservationRepo;
import com.mani.HotelBookingApp.Repository.RoomRepo;
import com.mani.HotelBookingApp.Repository.UserRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingService {
    private final ReservationRepo reservationRepo;
    private final UserRepo userRepo;
    private final RoomRepo roomRepo;
    private static final int SEARCH_RESULT_PER_PAGE = 4;

    public BookingService(ReservationRepo reservationRepo, UserRepo userRepo, RoomRepo roomRepo) {
        this.reservationRepo = reservationRepo;
        this.userRepo = userRepo;
        this.roomRepo = roomRepo;
    }

    public boolean postReservation(ReservationDto reservationDto) {
        Optional<User> user = userRepo.findById(reservationDto.getUserId());
        Optional<Room> room = roomRepo.findById(reservationDto.getRoomId());
        if (room.isPresent() && user.isPresent()) {
            if (reservationDto.getCheckOutDate().isBefore(reservationDto.getCheckInDate())) {
                return false;
            }
            Reservation reservation = new Reservation();
            reservation.setRoom(room.get());
            reservation.setUser(user.get());
            reservation.setCheckInDate(reservationDto.getCheckInDate());
            reservation.setCheckOutDate(reservationDto.getCheckOutDate());
            reservation.setReservationStatus(ReservationStatus.PENDING);

            Long days = ChronoUnit.DAYS.between(reservationDto.getCheckInDate(), reservationDto.getCheckOutDate());
            reservation.setPrice(room.get().getPrice() * days);
            reservationRepo.save(reservation);
            return true;
        }
        return false;
    }

    public ReservationResponseDto getAllReservationByUserId(Long userId, int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, SEARCH_RESULT_PER_PAGE);
        Page<Reservation> reservationPage = reservationRepo.findAllByUserId(pageable, userId);
        ReservationResponseDto reservationResponseDto = new ReservationResponseDto();
        reservationResponseDto.setReservationDto(reservationPage.stream().map(Reservation::getReservationDto).collect(Collectors.toList()));
        reservationResponseDto.setPageNo(reservationPage.getPageable().getPageNumber());
        reservationResponseDto.setTotalPages(reservationPage.getTotalPages());
        return reservationResponseDto;
    }
}
