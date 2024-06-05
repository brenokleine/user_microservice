package com.example.user_microservice.controllers;

import com.example.user_microservice.dtos.UserRecordDto;
import com.example.user_microservice.models.UserModel;
import com.example.user_microservice.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/users")
    public ResponseEntity<?> saveUser(@RequestBody @Valid UserRecordDto userRecordDto) {

        // Create a new UserModel object with the data from the UserRecordDto object
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userRecordDto, userModel);

        try{
            UserModel existingUser = userService.getUserByEmail(userModel.getEmail());

            if(existingUser != null){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("User with email " + userModel.getEmail() + " already exists.");
            } else {
                UserModel savedUser = userService.saveUser(userModel);
                return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
