package com.mani.HotelBookingApp.Controller;//NOSONAR

import com.mani.HotelBookingApp.DTO.*;
import com.mani.HotelBookingApp.Entity.User;
import com.mani.HotelBookingApp.Service.Jwt.UserServiceimpl;
import com.mani.HotelBookingApp.Service.UserService;
import com.mani.HotelBookingApp.Util.JWTUtils;
import jakarta.persistence.EntityExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin("http://localhost:4200")
@RequestMapping("/api")
public class UserController {
    private final UserService service;
    private final AuthenticationManager manager;
    private final UserServiceimpl serviceimpl;
    private final JWTUtils jwtUtils;

    public UserController(UserService service, AuthenticationManager manager, UserServiceimpl serviceimpl, JWTUtils jwtUtils) {
        this.service = service;
        this.manager = manager;
        this.serviceimpl = serviceimpl;
        this.jwtUtils = jwtUtils;
    }

    @RequestMapping(value = "/signUp",method = RequestMethod.POST)
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
        System.out.println(userDetails.getUsername());
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

    @GetMapping("/user-info")
    public ResponseEntity<?> getUserInfo() {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        System.out.println("priciple :"+principal.getClass().getName());
        System.out.println(principal);
        User user;
        if(principal instanceof UserDetails){
            String username= ((UserDetails) principal).getUsername();
            user=service.findByEmail(username).orElseThrow(()->new RuntimeException("User NOt Found"));
            ResponseEntity.status(HttpStatus.OK).body(user);
        } else if (principal instanceof UserDetails) {
           ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not Authenticated");
        }
        else {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Unknown bPrinciple");
        }
        return ResponseEntity.ok().build();
    }
}
