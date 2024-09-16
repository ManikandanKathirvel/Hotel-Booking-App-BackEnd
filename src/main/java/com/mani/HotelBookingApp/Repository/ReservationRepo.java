package com.mani.HotelBookingApp.Repository;

import com.mani.HotelBookingApp.Entity.Reservation;
import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepo extends JpaRepository<Reservation,Long> {
}
