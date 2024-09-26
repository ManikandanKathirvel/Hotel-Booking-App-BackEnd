package com.mani.HotelBookingApp.Service.Admin.Reservation;//NOSONAR

import com.mani.HotelBookingApp.DTO.ReservationResponseDto;
import com.mani.HotelBookingApp.Entity.Reservation;
import com.mani.HotelBookingApp.Entity.Room;
import com.mani.HotelBookingApp.Enum.ReservationStatus;
import com.mani.HotelBookingApp.Repository.ReservationRepo;
import com.mani.HotelBookingApp.Repository.RoomRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private final ReservationRepo reservationRepo;
    private final RoomRepo repo;

    public ReservationService(ReservationRepo reservationRepo, RoomRepo repo) {
        this.reservationRepo = reservationRepo;
        this.repo = repo;
    }
    public static final int SEARCH_RESULT_PER_PAGE = 4;
    public ReservationResponseDto getAllReservation(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, SEARCH_RESULT_PER_PAGE);
        Page<Reservation> reservationPage = reservationRepo.findAll(pageable);
        ReservationResponseDto reservationResponseDto = new ReservationResponseDto();
        reservationResponseDto.setReservationDto(reservationPage.stream().map(Reservation::getReservationDto).collect(Collectors.toList()));
        reservationResponseDto.setPageNo(reservationPage.getPageable().getPageNumber());
        reservationResponseDto.setTotalPages(reservationPage.getTotalPages());
        return reservationResponseDto;
    }

    public boolean changeReservationStatus(Long id, String status) {
        Optional<Reservation> reservation = reservationRepo.findById(id);
        if (reservation.isPresent()) {
            Reservation res = reservation.get();
            if (Objects.equals(status, "Approve")) {
                res.setReservationStatus(ReservationStatus.APPROVED);
            } else {
                res.setReservationStatus(ReservationStatus.REJECTED);
            }
            reservationRepo.save(res);
            Room room = res.getRoom();
            room.setAvailable(false);
            repo.save(room);
            return true;
        }
        return false;
    }
}
