package com.mani.HotelBookingApp.Repository;

import com.mani.HotelBookingApp.Entity.User;
import com.mani.HotelBookingApp.Enum.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUserRole(UserRole userRole);
}
