package com.apiRest.VUTTR.services;

import com.apiRest.VUTTR.dtos.UserRegisterDTO;
import com.apiRest.VUTTR.entities.User;
import com.apiRest.VUTTR.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void registerUser(UserRegisterDTO dto) {
        var user = new User(dto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void softDeleteAccount(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found for id: " + id + "."));

        if(user.isDeleted()) {
            throw new IllegalStateException("User already deleted.");
        }

        user.setDeletedAt(LocalDateTime.now());
    }

}
