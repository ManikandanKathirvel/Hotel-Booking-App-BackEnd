package com.mani.HotelBookingApp.ServiceTest;

import com.mani.HotelBookingApp.DTO.SignupRequest;
import com.mani.HotelBookingApp.DTO.UserDTO;
import com.mani.HotelBookingApp.Entity.User;
import com.mani.HotelBookingApp.Enum.UserRole;
import com.mani.HotelBookingApp.Repository.UserRepo;
import com.mani.HotelBookingApp.Service.UserService;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
 class UserServiceTest {
    @InjectMocks
    UserService userService;
    @Mock
    private UserRepo userRepo;
    @Mock
    private BCryptPasswordEncoder encoder;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testCreateUser_success(){
        SignupRequest signupRequest=new SignupRequest();
        signupRequest.setUsername("mani");
        signupRequest.setPassword("mani");
        signupRequest.setEmail("mani@9789");
        User user= new User();
        user.setUserRole(UserRole.CUSTOMER);
        user.setPassword("$2a$10$validEncryptedPassword");
        user.setEmail(signupRequest.getEmail());
        user.setUsername(signupRequest.getUsername());
        when(userRepo.findByEmail(signupRequest.getEmail())).thenReturn(Optional.empty());
        when(encoder.encode(signupRequest.getPassword())).thenReturn("$2a$10$validEncryptedPassword");
        when(userRepo.save(any(User.class))).thenReturn(user);
        UserDTO dto=userService.createUser(signupRequest);
        dto.setUsername(signupRequest.getUsername());
        assertNotNull(dto);
        assertEquals("mani",dto.getUsername());
        verify(userRepo).save(any(User.class));
    }
    @Test
    void testCreateUser_UserAlreadyExists(){
        SignupRequest signupRequest=new SignupRequest();
        signupRequest.setUsername("mani");
        signupRequest.setPassword("mani");
        signupRequest.setEmail("mani@9789");
        User user= new User();
        user.setUserRole(UserRole.CUSTOMER);
        user.setPassword("$2a$10$validEncryptedPassword");
        user.setEmail(signupRequest.getEmail());
        user.setUsername(signupRequest.getUsername());
        when(userRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(new User()));
        EntityExistsException exception=assertThrows(EntityExistsException.class,()->{
            userService.createUser(signupRequest);
        });
        assertEquals("user Already Present with username"+signupRequest.getEmail(),exception.getMessage());
        verify(userRepo,never()).save(any(User.class));
    }
    @Test
    void testCreateUser_passwordEncoding(){
        SignupRequest signupRequest=new SignupRequest();
        signupRequest.setUsername("mani");
        signupRequest.setPassword("mani");
        signupRequest.setEmail("mani@9789");

        User user= new User();
        user.setUserRole(UserRole.CUSTOMER);
        user.setPassword(encoder.encode(signupRequest.getPassword()));
        user.setEmail(signupRequest.getEmail());
        user.setUsername(signupRequest.getUsername());

        UserDTO dto=new UserDTO();
        dto.setUsername("mani");
        dto.setEmail("mani@9789");
        dto.setPassword(user.getPassword());

        when(userRepo.findByEmail(signupRequest.getEmail())).thenReturn(Optional.empty());
        when(encoder.encode(signupRequest.getPassword())).thenReturn(user.getPassword());
        when(userRepo.save(any(User.class))).thenReturn(user);
        UserDTO userDTO=userService.createUser(signupRequest);

        assertNotNull(userDTO);
        assertEquals("mani",userDTO.getUsername());
        assertEquals(user.getPassword(),userDTO.getPassword());

        verify(encoder).encode(signupRequest.getPassword());
    }
    @Test
    void testCreateAdminAcc_AdminAccountDoesNotExist(){
        when(userRepo.findByUserRole(UserRole.ADMIN)).thenReturn(Optional.empty());
        userService.createAdminAcc();
        verify(userRepo).save(any(User.class));
    }
//    @Test
//    void testCreateAdminAcc_AdminAccountAlreadyExists(){
//        User user=new User();
//        user.setUsername("manikm");
//        when(userRepo.findByUserRole(UserRole.ADMIN)).thenReturn(Optional.of(user));
//        when(userRepo.findByEmail(user.getUsername())).thenThrow(new RuntimeException("acc already exist"));
//        Exception exception=assertThrows(RuntimeException.class,()->{
//            userService.createAdminAcc();
//        });
//
//        assertEquals("acc already exist",exception.getMessage());
//        verify(userRepo,never()).save(any(User.class));
//    }
    @Test
    void testFindByEmail_UserExists(){
        String mail="mani@9789";
        User user=new User();
        user.setUsername("mani");
        user.setPassword("mani");
        user.setEmail(mail);
        when(userRepo.findByEmail(mail)).thenReturn(Optional.of(user));
        Optional<User> result=userService.findByEmail(mail);

        assertTrue(result.isPresent());
        assertEquals(mail,result.get().getEmail());
    }

    @Test
    void testFindByEmail_UserDoesNotExist(){
        String mail="mani@9789";
        when(userRepo.findByEmail(mail)).thenReturn(Optional.empty());
        Optional<User> result=userService.findByEmail(mail);
        assertFalse(result.isPresent());
    }
    @Test
    void testFindByEmail_NullEmail(){
        Optional<User> mail=userService.findByEmail(null);
        assertFalse(mail.isPresent());
        verify(userRepo,never()).findByEmail(anyString());
    }




}
