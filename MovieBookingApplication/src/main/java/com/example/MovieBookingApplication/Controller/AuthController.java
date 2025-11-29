package com.example.MovieBookingApplication.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import com.example.MovieBookingApplication.DTO.LoginRequestDTO;
import com.example.MovieBookingApplication.DTO.LoginResponseDTO;
import com.example.MovieBookingApplication.DTO.RegisterRequestDTO;
import com.example.MovieBookingApplication.Entity.User;
import com.example.MovieBookingApplication.Respository.UserRepository;
import com.example.MovieBookingApplication.Service.AuthenticationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private SecurityContextRepository securityContextRepository;
    
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/registernormaluser")
    public ResponseEntity<User> registerNormalUser(@RequestBody RegisterRequestDTO registerRequestDTO) {
        return ResponseEntity.ok(authenticationService.registerNormalUser(registerRequestDTO));
    }

    // === SESSION LOGIN LOGIC ===
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO, 
                                                  HttpServletRequest request, 
                                                  HttpServletResponse response) {
        
        // 1. Authenticate the user (Check username/password)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getUsername(),
                        loginRequestDTO.getPassword()
                )
        );
        
        // 2. Create a SecurityContext and set the Authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        
        // 3. SAVE CONTEXT TO SESSION (This creates the JSESSIONID cookie)
        // This is the equivalent of session.setAttribute("user", ...) in your sample project
        securityContextRepository.saveContext(context, request, response);

        // 4. Return user info to frontend
        User user = userRepository.findByUsername(loginRequestDTO.getUsername())
             .orElseThrow(() -> new RuntimeException("User not found"));
             
        LoginResponseDTO responseDTO = LoginResponseDTO.builder()
                .username(user.getUsername())
                .roles(user.getRoles())
                .build();

        return ResponseEntity.ok(responseDTO);
    }
    
    // Logout is handled automatically by SecurityConfig at /api/auth/logout
}