package com.ecommerce.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long  id;

    @NotBlank
    @Size(min = 3 , max = 15)
    private String userName;

    @NotBlank
    @Email
    @Size(max=25)
    private String email;

    @NotBlank
    @Size(max=30)
    private String password;

    public User( String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }
}
