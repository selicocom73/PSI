package com.example.psi.view;

import com.example.psi.request.RegisterRequest;
import com.example.psi.response.Response;
import com.example.psi.service.RegistrationService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.Route;
import org.springframework.stereotype.Component;

@Route("registration")
@Component
public class RegistrationView extends VerticalLayout {

    private final RegistrationService registrationService;
    private EmailField emailField = new EmailField("Email");
    private PasswordField passwordField = new PasswordField("Hasło");
    private Button submitButton = new Button("Zarejestruj", event -> handleSubmit());
    private Button backButton = new Button("Back", event -> UI.getCurrent().navigate(""));

    public RegistrationView(RegistrationService registrationService) {
        this.registrationService = registrationService;
        createView();
    }

    private void createView() {
        FormLayout formLayout = prepareFormLoginLayout();
        stylingView();
        add(new H3("PSI - rejestracja"));
        add(formLayout);
        add(backButton);
    }

    private void handleSubmit() {
        String emailValue = emailField.getValue();
        String passwordValue = passwordField.getValue();
        Response response = registrationService.register(new RegisterRequest(emailValue, passwordValue));
        if (response.isSuccess()) {
            Notification.show("Registration complete with success", 5000, Notification.Position.MIDDLE);

            UI.getCurrent().navigate("login");
        } else {
            Notification.show(response.error(), 5000, Notification.Position.MIDDLE);
        }
    }

    private FormLayout prepareFormLoginLayout() {
        FormLayout formLayout = new FormLayout();
        emailField.addKeyPressListener(Key.ENTER, e -> handleSubmit());
        passwordField.addKeyPressListener(Key.ENTER, e -> handleSubmit());
        formLayout.add(emailField, passwordField, submitButton);

        // Ustawienie formularza na środku ekranu
        formLayout.setMaxWidth("300px"); // Maksymalna szerokość formularza
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP)); // Ustawienie, aby etykiety były nad polami
        formLayout.getStyle().set("margin", "auto"); // Wycentrowanie formularza

        setAlignItems(FlexComponent.Alignment.CENTER); // Wycentrowanie zawartości w pionie
        setSizeFull(); // Ustawienie pełnej dostępnej wysokości widoku
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        formLayout.setMaxWidth("500px"); // Maksymalna szerokość formularza
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP)); // Ustawienie, aby etykiety były nad polami
        formLayout.getStyle().set("margin", "auto"); // Wycentrowanie formularza
        return formLayout;
    }

    private void stylingView() {
        setAlignItems(FlexComponent.Alignment.CENTER); // Wycentrowanie zawartości w pionie
        setSizeFull(); // Ustawienie pełnej dostępnej wysokości widoku
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
    }
}
