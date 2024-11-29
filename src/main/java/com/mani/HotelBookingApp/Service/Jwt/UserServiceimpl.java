package com.mani.HotelBookingApp.Service.Jwt;//NOSONAR

import com.mani.HotelBookingApp.Repository.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceimpl {
    Logger logger= LoggerFactory.getLogger(UserServiceimpl.class);
    private final UserRepo repo;
    public UserServiceimpl(UserRepo repo) {
        this.repo = repo;
    }

    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
                logger.info("email : "+email);
                return repo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };
    }
}
