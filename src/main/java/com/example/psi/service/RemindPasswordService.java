package com.example.psi.service;

import com.example.psi.entity.UserEntity;
import com.example.psi.repository.UserRepository;
import com.example.psi.response.RemindPasswordResponse;
import com.example.psi.singletone.Storage;
import com.mailersend.sdk.MailerSend;
import com.mailersend.sdk.MailerSendResponse;
import com.mailersend.sdk.emails.Email;
import com.mailersend.sdk.exceptions.MailerSendException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class RemindPasswordService {

    private static final Duration durationOfChangePassword = Duration.ofSeconds(20);


    private final UserRepository userRepository;


    public RemindPasswordService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public RemindPasswordResponse remindPassword(String email) {
        if (email.isBlank()) {
            return new RemindPasswordResponse("Invalid email");
        }
        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(email);
        if (userEntityOptional.isEmpty()) {
            return new RemindPasswordResponse("Not found email");
        }
        UserEntity userEntity = userEntityOptional.get();
        String currentPasswordSha1 = userEntity.getCurrentPasswordSha1();
        LocalDateTime timeOfCreatingPassword = userEntity.getPasswordEntities().stream()
                .filter(f -> f.getPasswordSha1().equals(currentPasswordSha1))
                .findFirst()
                .orElseThrow().getTimestamp();
        if (timeOfCreatingPassword.plus(durationOfChangePassword).isBefore(LocalDateTime.now())) {
            Storage.getInstance().setCurrentEmail(email);
            String codeToChangePassword = generateRandomString();
            userEntity.setCodeToChangePassword(codeToChangePassword);
            userRepository.save(userEntity);
            sendEmailWithCode(email, codeToChangePassword);
            return new RemindPasswordResponse(true);
        }
        return new RemindPasswordResponse("Can't remind a password that was created less than "+durationOfChangePassword.getSeconds()+" seconds ago");
    }


    private void sendEmailWithCode(String emailStr, String code) {
        Email email = new Email();

        email.setFrom("PSI application", "noreply@trial-x2p0347ek1ylzdrn.mlsender.net");
        email.addRecipient("PSI remind password", emailStr);

        email.setSubject("PSI code to remind password");

        email.setHtml("<p>Code to remind password: " + code + "</p>");

        MailerSend ms = new MailerSend();

        ms.setToken("mlsn.4ee96849aa7b4d3b9b8eacc5060152f03821d12fbdfe85ffc958ae6201739ad9");

        try {
            MailerSendResponse response = ms.emails().send(email);
            System.out.println("Send email with id: " + response.messageId + " with code: " + code);
        } catch (MailerSendException e) {
            e.printStackTrace();
        }
    }

    private String generateRandomString() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(4);

        for (int i = 0; i < 4; i++) {
            int digit = random.nextInt(10); // Losuje cyfrę od 0 do 9
            sb.append(digit);
        }

        return sb.toString();
    }
}
