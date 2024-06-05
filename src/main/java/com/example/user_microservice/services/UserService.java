package com.example.user_microservice.services;

import com.example.user_microservice.models.UserModel;
import com.example.user_microservice.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    //this annotation guarantees that the transaction will be committed or rolled back
    //for when I send a message in the message channel for the broker
    @Transactional
    public UserModel saveUser(UserModel userModel) {
        return userRepository.save(userModel);
    }

    public UserModel getUserById(UUID userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public void deleteUserById(UUID userId) {
        userRepository.deleteById(userId);
    }

    public UserModel getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
