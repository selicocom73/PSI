package com.example.psi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfiguration {


    @Bean
    public JavaMailSenderImpl getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.mailersend.net");
        mailSender.setPort(587);

        mailSender.setUsername("MS_D6e3ko@trial-x2p0347ek1ylzdrn.mlsender.net");
        mailSender.setPassword("jiCCN7SUDmPKd4UM");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.host", "smtp.mailersend.net");
        props.put("mail.debug", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.socketFactory.port", "587");
        props.put("mail.smtp.socketFactory.fallback", "false");

        return mailSender;
    }
}
