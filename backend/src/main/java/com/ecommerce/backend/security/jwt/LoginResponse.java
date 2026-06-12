package com.ecommerce.backend.security.jwt;

import com.ecommerce.backend.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String jwtToken;
    private String username;

    private List<Role> roles;


}
