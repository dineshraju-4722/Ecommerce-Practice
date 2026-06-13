package com.ecommerce.backend.controller;

import com.ecommerce.backend.model.AppRole;
import com.ecommerce.backend.model.Role;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.repositories.RoleRepository;
import com.ecommerce.backend.repositories.UserRepository;
import com.ecommerce.backend.security.jwt.JwtUtils;
import com.ecommerce.backend.security.request.LoginRequest;
import com.ecommerce.backend.security.request.SignupRequest;
import com.ecommerce.backend.security.response.LoginResponse;
import com.ecommerce.backend.security.response.MessageResponse;
import com.ecommerce.backend.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest){
        Authentication authentication;
        try{
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
        }
        catch(AuthenticationException exception){
            Map<String,Object> map = new HashMap<>();
            map.put("message","Bad Credintials");
            map.put("status",false);
            return new ResponseEntity<Object>(
                    map, HttpStatus.NOT_FOUND
            );
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        assert principal != null;
        String jwtToken = jwtUtils.generateTokenFromUsername(principal);

        List<String> roles = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        LoginResponse loginResponse = new LoginResponse(jwtToken, principal.getUsername(), roles);
        return ResponseEntity.ok(loginResponse);

    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest){
        if(userRepository.existsByUserName(signupRequest.getUsername())){
            return ResponseEntity.badRequest().body(
                    new MessageResponse("Error: Username already taken")
            );
        }
        if(userRepository.existsByEmail(signupRequest.getEmail())){
            return ResponseEntity.badRequest().body(
                    new MessageResponse("Error: Email already taken")
            );
        }
        User user = new User(signupRequest.getUsername(),
                signupRequest.getEmail(),
                passwordEncoder.encode(signupRequest.getPassword())
                );
        Set<String> strRoles = signupRequest.getRole();
        Set<Role> roles = new HashSet<>();
        if(strRoles==null){
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER).orElseThrow(
                    ()-> new RuntimeException("Error: Role not found")
            );
            roles.add(userRole);
        }
        else{
            strRoles.forEach( role -> {
                switch(role){
                    case "admin" :
                        Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error : Role doesnot found"));
                        roles.add(adminRole);
                        break;
                    case "seller":
                        Role sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
                                .orElseThrow(() -> new RuntimeException("Error : Role does not found"));
                        roles.add(sellerRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error : Role does not found"));
                        roles.add(userRole);
                        break;
                }
            } );
        }
        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok
                (new MessageResponse("User Registered Successfully"));

    }
}
