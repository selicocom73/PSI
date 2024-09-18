package com.example.psi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Session {

    private Long id;
    private LocalDateTime start;

    private LocalDateTime stop;
}
