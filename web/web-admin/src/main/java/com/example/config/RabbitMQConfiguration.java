package com.example.config;


import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

    @Bean("amq_direct")
    public Exchange directExchange() {
        return ExchangeBuilder
                .directExchange("amq.direct")
                .build();
    }

    @Bean("submit_order_queue")
    public Queue submitOrderQueue() {
        return QueueBuilder
                .nonDurable("submit_order_queue")
                .build();
    }


    @Bean("submit_queue_and_direct_exchange_binding")
    public Binding submitQueueAndDirectExchangeBinding(@Qualifier("amq_direct") Exchange exchange,
                                                       @Qualifier("submit_order_queue") Queue queue) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with("submit_order")
                .noargs();
    }


    @Bean
    public Jackson2JsonMessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

}
