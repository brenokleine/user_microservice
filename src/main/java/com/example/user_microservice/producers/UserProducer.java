package com.example.user_microservice.producers;

import com.example.user_microservice.dtos.EmailDto;
import com.example.user_microservice.models.UserModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserProducer {
    //here are sent the messages for the broker
    //in the respective queue

    final RabbitTemplate rabbitTemplate;

    public UserProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value("${broker.queue.email.name}")
    private String routingKey;

    public void publishMessageEmail(UserModel userModel){

        var emailDto = new EmailDto();
        emailDto.setUserId(userModel.getUserId());
        emailDto.setEmailTo(userModel.getEmail());
        emailDto.setSubject("Welcome to our platform!");
        emailDto.setText("Hello " + userModel.getName() + ", \nWelcome to our platform!");

        rabbitTemplate.convertAndSend("", routingKey, emailDto);
    }


}
