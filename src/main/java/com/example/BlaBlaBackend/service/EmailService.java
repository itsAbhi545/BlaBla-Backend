package com.example.BlaBlaBackend.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {
    @Autowired
    JavaMailSender mailSender;
    @Bean(name = "threadPoolTaskExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(30);
        executor.initialize();
        return executor;
    }

    public void sendOtpMessage(String to, String subject, String message) throws MessagingException {

        MimeMessage msg = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        helper.setFrom("Mervron_Boss@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(message, true);
        mailSender.send(msg);
    }
    @Async("threadPoolTaskExecutor")
    public void sendPasswordResetLink(String to, String subject, String message) throws MessagingException {

        MimeMessage msg = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        helper.setFrom("BlaBla_Boss@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(message, true);
        helper.addInline("clickLink", new ClassPathResource("img/avatar.jpg"));
        mailSender.send(msg);
    }
}
