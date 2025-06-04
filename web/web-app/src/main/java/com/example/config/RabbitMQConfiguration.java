package com.example.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

    @Bean
    @Qualifier("txRabbitTemplate")
    public RabbitTransactionManager rabbitTxManager(ConnectionFactory cf) {
        return new RabbitTransactionManager(cf);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory cf, MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(cf);
        template.setChannelTransacted(true);
        template.setReplyTimeout(5_000);
        template.setMessageConverter(messageConverter);
        return template;
    }

    @Bean("rpcRabbitTemplate")
    public RabbitTemplate rpcRabbitTemplate(ConnectionFactory cf,
                                            MessageConverter converter) {
        RabbitTemplate tpl = new RabbitTemplate(cf);
        // JSON 序列化
        tpl.setMessageConverter(converter);
        // 阻塞等待 5 秒
        tpl.setReplyTimeout(5_000);
        // 临时队列模式，避免 Direct-Reply-To 监听器
        tpl.setUseTemporaryReplyQueues(true);
        // 关键：RPC 通道不走事务
        tpl.setChannelTransacted(false);
        return tpl;
    }

    @Bean("email")
    public Exchange emailExchange() {
        return ExchangeBuilder
                .directExchange("email")
                .build();
    }

    @Bean("cart")
    public Exchange directExchange() {
        return ExchangeBuilder
                .directExchange("cart")
                .build();
    }

    @Bean("pay")
    public Exchange directExchangePay() {
        return ExchangeBuilder
                .directExchange("pay")
                .build();

    }

    @Bean("dlPay")
    public Exchange dlxExchange() {
        return ExchangeBuilder
                .directExchange("dlx.pay")
                .build();
    }

    @Bean("emailQueue")
    public Queue emailQueue() {
        return QueueBuilder
                .durable("mail")
                .build();
    }

    @Bean("pay_queue")
    public Queue payQueue() {
        return QueueBuilder
                .nonDurable("pay_queue")
                .deadLetterRoutingKey("pay_outdated")
                .deadLetterExchange("dlx.pay")
                .ttl(24 * 60 * 60 * 1000)
                .build();
    }

    @Bean("pay_outdated_queue")
    public Queue payOutdatedQueue() {
        return QueueBuilder
                .nonDurable("pay_outdated_queue")
                .build();
    }

    @Bean("add_cartItem_queue")
    public Queue addCartItemQueue() {
        return QueueBuilder
                .nonDurable("add_cartItem_queue")
                .build();
    }


    @Bean("delete_cartItem_queue")
    public Queue deleteCartItemQueue() {
        return QueueBuilder
                .nonDurable("delete_cartItem_queue")
                .build();
    }

    @Bean("update_cartItem_queue")
    public Queue updateCartItemQueue() {
        return QueueBuilder
                .nonDurable("update_cartItem_queue")
                .build();
    }

    @Bean("submit_cart_queue")
    public Queue submitCartQueue() {
        return QueueBuilder
                .nonDurable("submit_cart_queue")
                .build();
    }

    @Bean("email_send_binding")
    public Binding emailBinding(@Qualifier("email") Exchange exchange,
                                @Qualifier("emailQueue") Queue queue) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with("send_email")
                .noargs();
    }

    @Bean("pay_binding")
    public Binding payBinding(@Qualifier("pay") Exchange exchange,
                              @Qualifier("pay_queue") Queue queue) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with("pay")
                .noargs();
    }

    @Bean("pay_outdated_binding")
    public Binding payOutdatedBinding(@Qualifier("dlPay") Exchange exchange,
                                      @Qualifier("pay_outdated_queue") Queue queue) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with("pay_outdated")
                .noargs();
    }

    @Bean("add_cartItem_binding")
    public Binding addCartItemBinding(@Qualifier("cart") Exchange exchange,
                                      @Qualifier("add_cartItem_queue") Queue queue) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with("add_cartItem")
                .noargs();
    }

    @Bean("delete_cartItem_binding")
    public Binding deleteCartItemBinding(@Qualifier("cart") Exchange exchange,
                                         @Qualifier("delete_cartItem_queue") Queue queue) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with("delete_cartItem")
                .noargs();
    }

    @Bean("update_cartItem_binding")
    public Binding updateCartItemBinding(@Qualifier("cart") Exchange exchange,
                                         @Qualifier("update_cartItem_queue") Queue queue) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with("update_cartItem")
                .noargs();
    }

    @Bean("submit_cart_binding")
    public Binding submitCartBinding(@Qualifier("cart") Exchange exchange,
                                     @Qualifier("submit_cart_queue") Queue queue) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with("submit_cart")
                .noargs();
    }


    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }


}
