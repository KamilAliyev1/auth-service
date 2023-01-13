package com.authservice.config;

import com.authservice.config.properties.RabbitConfigProperties;
import lombok.Data;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Data
@Configuration
public class RabbitMQConfig {

    private final RabbitConfigProperties rabbitProperties;


    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Bean
    public Queue queue(){
        return new Queue(rabbitProperties.getQue());
    }

    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(rabbitProperties.getExchange());
    }

    @Bean
    public Binding binding(final Queue queue, final DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(rabbitProperties.getRoute());
    }
}
