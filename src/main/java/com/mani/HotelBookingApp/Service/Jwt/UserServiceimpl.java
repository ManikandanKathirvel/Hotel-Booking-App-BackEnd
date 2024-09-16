package com.mani.HotelBookingApp.Service.Jwt;

import com.mani.HotelBookingApp.Enum.UserRole;
import com.mani.HotelBookingApp.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceimpl {
    @Autowired
    UserRepo repo;
    public UserDetailsService userDetailsService(){
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
                return repo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));

            }
        };
    }
}
