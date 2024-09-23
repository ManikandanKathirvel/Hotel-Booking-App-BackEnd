package com.mani.HotelBookingApp.Service.Customer.Booking;//NOSONAR

import com.mani.HotelBookingApp.DTO.ReservationDto;
import com.mani.HotelBookingApp.Entity.Reservation;
import com.mani.HotelBookingApp.Entity.Room;
import com.mani.HotelBookingApp.Entity.User;
import com.mani.HotelBookingApp.Enum.ReservationStatus;
import com.mani.HotelBookingApp.Repository.ReservationRepo;
import com.mani.HotelBookingApp.Repository.RoomRepo;
import com.mani.HotelBookingApp.Repository.UserRepo;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class BookingService {

    private  final ReservationRepo reservationRepo;
    private final UserRepo userRepo;
    private final RoomRepo roomRepo;

    public BookingService(ReservationRepo reservationRepo, UserRepo userRepo, RoomRepo roomRepo) {
        this.reservationRepo = reservationRepo;
        this.userRepo = userRepo;
        this.roomRepo = roomRepo;
    }

    public boolean postReservation(ReservationDto reservationDto) {
        Optional<User> user = userRepo.findById(reservationDto.getUserId());
        Optional<Room> room = roomRepo.findById(reservationDto.getRoomId());

        if (room.isPresent() && user.isPresent()) {
            Reservation reservation = new Reservation();
            reservation.setRoom(room.get());
            reservation.setUser(user.get());
            reservation.setCheckInDate(reservationDto.getCheckInDate());
            reservation.setCheckOutDate(reservationDto.getCheckOutDate());
            reservation.setReservation(ReservationStatus.PENDING);

            Long days = ChronoUnit.DAYS.between(reservationDto.getCheckInDate(), reservationDto.getCheckOutDate());
            reservation.setPrice(room.get().getPrice() * days);

            reservationRepo.save(reservation);
            return true;
        }
        return false;
    }

}
