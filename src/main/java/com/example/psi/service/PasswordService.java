package com.example.psi.service;

import com.example.psi.model.PasswordResult;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {

    public PasswordResult countStrangeOfPassword(String password) {
        PasswordResult passwordResult = new PasswordResult();
        int strangeOfPassword = 0;
        if(isOkLength(password)) {
            strangeOfPassword += 1;
        }else{
            passwordResult.setOk(false);
            passwordResult.setError("Password is too short");
            passwordResult.setStrangeOfPassword(strangeOfPassword);
            return passwordResult;
        }

        if(passwordContainDigits(password)) {
            strangeOfPassword += 1;
        } else {
            passwordResult.setOk(false);
            passwordResult.setError("Password not contain digits");
            passwordResult.setStrangeOfPassword(strangeOfPassword);
            return passwordResult;
        }

        if(passwordContainLowerCase(password)) {
            strangeOfPassword += 1;
        } else {
            passwordResult.setOk(false);
            passwordResult.setError("Password not contain lowercase");
            passwordResult.setStrangeOfPassword(strangeOfPassword);
            return passwordResult;
        }

        if(passwordContainUpperCase(password)) {
            strangeOfPassword += 1;
        } else {
            passwordResult.setOk(false);
            passwordResult.setError("Password not contain uppercase");
            passwordResult.setStrangeOfPassword(strangeOfPassword);
            return passwordResult;
        }

        if(passwordContainSpecialCharacter(password)) {
            strangeOfPassword += 1;
        } else {
            passwordResult.setOk(false);
            passwordResult.setError("Password not contain special character as [!@#$%^&*(),.?\":{}|<>].");
            passwordResult.setStrangeOfPassword(strangeOfPassword);
            return passwordResult;
        }
        passwordResult.setOk(true);
        passwordResult.setStrangeOfPassword(strangeOfPassword);
        return passwordResult;
    }

    private boolean isOkLength(String password) {
        return password.length() >= 8;
    }

    private boolean passwordContainDigits(String password) {
        return password.matches(".*\\d.*");
    }

    private boolean passwordContainLowerCase(String password) {
        return password.matches(".*[a-z].*");
    }

    private boolean passwordContainUpperCase(String password) {
        return password.matches(".*[A-Z].*");
    }

    private boolean passwordContainSpecialCharacter(String password) {
        return password.matches(".*[!@#$%^&*(),.?\":{}|<>].*");
    }
}
