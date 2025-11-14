package com.apiRest.VUTTR.entities;

import com.apiRest.VUTTR.dtos.UserRegisterDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@AllArgsConstructor
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Setter
    @Column(nullable = false)
    private String password;

    public User(UserRegisterDTO dto) {
        this.email = dto.email();
        this.password = dto.password();
    }

}
