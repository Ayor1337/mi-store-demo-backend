package com.example.listener;

import com.rabbitmq.client.Channel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@RabbitListener(queues = "mail")
public class SendEmailListener {
    @Resource
    private JavaMailSender sender;

    @Resource
    private RabbitTemplate rabbitTemplate; // RabbitMQ 发送/接收消息的模板

    @Value("${spring.mail.username}")
    private String username;

    @Transactional(rollbackFor = Exception.class)
    @RabbitHandler
    public void sendMailMessage(Map<String, Object> data,
                                Message message,
                                Channel channel) throws IOException {
        try {
            String email = (String) data.get("email");
            Integer code = (Integer) data.get("code");

            SimpleMailMessage mailMessage = createMessage("您的密码重置邮件",
                    "您好，您正在进行重置密码操作，验证码:" + code + "，有效时间3分钟，为了保障您的安全，请勿向他人泄露你的验证码信息",
                    email);

            sender.send(mailMessage);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            // 清空 RabbitMQ 队列，防止堆积无效邮件任务
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
        }
    }

    private SimpleMailMessage createMessage(String title, String content, String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(title);
        message.setText(content);
        message.setTo(email);
        message.setFrom(username);

        return message;
    }
}
