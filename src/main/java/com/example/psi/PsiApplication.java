package com.example.psi;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PsiApplication implements AppShellConfigurator {

	public static void main(String[] args) {
		SpringApplication.run(PsiApplication.class, args);
	}

}
