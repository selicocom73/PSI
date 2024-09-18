package com.example.psi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class InvalidLogin {

    private Long id;

    private LocalDateTime localDateTime;
}
