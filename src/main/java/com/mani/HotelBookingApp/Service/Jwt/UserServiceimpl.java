package com.mani.HotelBookingApp.Service.Jwt;//NOSONAR

import com.mani.HotelBookingApp.Repository.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceimpl {
    private final UserRepo repo;

    public UserServiceimpl(UserRepo repo) {
        this.repo = repo;
    }

    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
                System.out.println(email);
                return repo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };
    }
}
