package com.mani.HotelBookingApp.Controller;

import com.mani.HotelBookingApp.DTO.AuthenticationResponse;
import com.mani.HotelBookingApp.DTO.AuthenticationRequest;
import com.mani.HotelBookingApp.DTO.SignupRequest;
import com.mani.HotelBookingApp.DTO.UserDTO;
import com.mani.HotelBookingApp.Entity.User;
import com.mani.HotelBookingApp.Service.Jwt.UserServiceimpl;
import com.mani.HotelBookingApp.Service.UserService;
import com.mani.HotelBookingApp.Util.JWTUtils;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin("http://localhost:4200")
@RequestMapping("/api")
public class UserController {
    @Autowired
    UserService service;
    @Autowired
    AuthenticationManager manager;

    @Autowired
    UserServiceimpl serviceimpl;
    @Autowired
    JWTUtils jwtUtils;

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody SignupRequest signupRequest) {
        try {
            UserDTO dto = service.createUser(signupRequest);
            return ResponseEntity.status(HttpStatus.OK).body(dto);
        } catch (EntityExistsException ex) {
            return new ResponseEntity<>("userAll ready Exist", HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            return new ResponseEntity<>("Email id not available", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            manager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("incorrect email or password");
        }
        final UserDetails userDetails = serviceimpl.userDetailsService().loadUserByUsername(authenticationRequest.getEmail());
        Optional<User> optionalUser = service.findByEmail(userDetails.getUsername());
        final String jwt = jwtUtils.generateToken(userDetails);

        AuthenticationResponse response = new AuthenticationResponse();
        if (optionalUser.isPresent()) {
            response.setJwt(jwt);
            response.setUserRole(optionalUser.get().getUserRole());
            response.setUserId(optionalUser.get().getId());
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
