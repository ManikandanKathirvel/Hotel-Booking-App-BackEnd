package com.mani.HotelBookingApp.Repository;//NOSONAR

import com.mani.HotelBookingApp.Entity.User;
import com.mani.HotelBookingApp.Enum.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;//NOSONAR

import java.util.Optional;

public interface UserRepo extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUserRole(UserRole userRole);
}
