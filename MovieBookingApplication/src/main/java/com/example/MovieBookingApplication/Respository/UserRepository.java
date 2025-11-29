package com.example.MovieBookingApplication.Respository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.MovieBookingApplication.Entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    // Optional: find user by email for login
    Optional<User> findByUsername(String username);

}
