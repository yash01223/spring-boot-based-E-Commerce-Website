package com.spring.ecommerce.E_commerce.website.Service;

import com.spring.ecommerce.E_commerce.website.Entity.User;
import com.spring.ecommerce.E_commerce.website.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.passwordEncoder=passwordEncoder;
        this.userRepository=userRepository;
    }

    public boolean saveUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            // User already exists, handle accordingly
            return false;
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(new HashSet<>(List.of("ROLE_USER")));
        userRepository.save(user);
        return true;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return (UserDetails) (userRepository.findByEmail(email))
                .orElseThrow(()-> new UsernameNotFoundException("User with"+email +"not found"));
    }
}