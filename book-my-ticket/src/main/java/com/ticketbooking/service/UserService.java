package com.ticketbooking.service;

import com.ticketbooking.dto.LoginDto;
import com.ticketbooking.dto.UserDto;
import com.ticketbooking.exception.InvalidBookingException;
import com.ticketbooking.exception.ResourceNotFoundException;
import com.ticketbooking.model.User;
import com.ticketbooking.model.enums.Role;
import com.ticketbooking.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto register(UserDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new InvalidBookingException("Email already registered");
        }
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRole(dto.getRole() != null ? Role.valueOf(dto.getRole()) : Role.USER);
        user = userRepository.save(user);
        return toDto(user);
    }

    public UserDto login(LoginDto dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + dto.getEmail()));
        if (!user.getPassword().equals(dto.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        return toDto(user);
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return toDto(user);
    }

    private UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().name());
        return dto;
    }

}
