package com.mani.HotelBookingApp.Repository;//NOSONAR

import com.mani.HotelBookingApp.Entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ReservationRepo extends JpaRepository<Reservation, Long> {
    Page<Reservation> findAllByUserId(Pageable pageable, Long userId);
}
