package com.nt.CakeService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nt.model.User;
import com.nt.repository.IUserRepository;

@Service
public class UserService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String registerUser(String username, String password, String email) {
        if (userRepository.existsByUsername(username)) {
            return "Username already exists";
        }
        if (userRepository.existsByEmail(email)) {
            return "Email already exists";
        }

        User user = new User(username, passwordEncoder.encode(password), email, "ROLE_USER");
        userRepository.save(user);
        return null; // Success
    }
}