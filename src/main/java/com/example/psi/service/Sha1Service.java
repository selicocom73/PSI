package com.example.psi.service;

import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class Sha1Service {

    private final MessageDigest messageDigest;

    public Sha1Service() throws NoSuchAlgorithmException {
        messageDigest = MessageDigest.getInstance("SHA-1");
    }
    public String sha1(String input) {
        byte[] result = messageDigest.digest(input.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : result) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
