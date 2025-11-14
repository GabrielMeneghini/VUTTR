package com.apiRest.VUTTR.services;

import com.apiRest.VUTTR.dtos.UserRegisterDTO;
import com.apiRest.VUTTR.entities.User;
import com.apiRest.VUTTR.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

}
