package com.example.psi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PasswordResult {

    private String error;
    private  boolean isOk;
    private int strangeOfPassword;
}
