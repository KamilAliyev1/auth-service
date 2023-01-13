package com.authservice.service;

import com.authservice.config.properties.RabbitConfigProperties;
import com.authservice.dto.MailDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;




@Data
@Service
public class BrokerService {

    private final ObjectMapper objectMapper;

    private final RabbitTemplate rabbitTemplate;

    private final RabbitConfigProperties rabbitConfigProperties;



    void sendToQueue(MailDTO mailDTO){
        String notif;
        try {
            notif = objectMapper.writeValueAsString(mailDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        rabbitTemplate.convertAndSend(
                rabbitConfigProperties.getExchange()
                ,rabbitConfigProperties.getRoute()
                ,notif);
    }

}
