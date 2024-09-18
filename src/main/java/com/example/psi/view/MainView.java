package com.example.psi.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.stereotype.Component;

@Route("")
@Component
public class MainView extends VerticalLayout {

    public MainView() {
        // Tworzenie przycisków
        Button signInButton = new Button("Sign In", event -> UI.getCurrent().navigate("login"));
        Button signUpButton = new Button("Sign Up", event -> UI.getCurrent().navigate("registration"));

        // Tworzenie poziomego układu dla przycisków
        HorizontalLayout buttonLayout = new HorizontalLayout(signInButton, signUpButton);

        // Wycentrowanie przycisków
        buttonLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        // Wycentrowanie pionowego układu
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeFull();

        // Dodanie poziomego układu do głównego pionowego układu
        add(new H3("PSI - Strona główna"));
        add(buttonLayout);
    }
}