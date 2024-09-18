package com.example.psi.view;

import com.example.psi.request.LoginRequest;
import com.example.psi.request.RegisterRequest;
import com.example.psi.response.Response;
import com.example.psi.service.LoginService;
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
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import org.springframework.stereotype.Component;

@Route("login")
@Component
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private final LoginService loginService;
    private final EmailField emailField = new EmailField("Email");
    private final PasswordField passwordField = new PasswordField("Hasło");
    private final Button submitButton = new Button("Zaloguj", event -> handleSubmit());
    private final Button remindPasswordButton;

    public LoginView(LoginService loginService) {
        this.loginService = loginService;
        remindPasswordButton = new Button("Przypomnij hasło", event -> remindPassword());
        createView();
    }

    private void createView() {
        H3 header = new H3("PSI - logowanie");
        FormLayout formLayout = new FormLayout();

        emailField.addKeyPressListener(Key.ENTER, e -> handleSubmit());
        passwordField.addKeyPressListener(Key.ENTER, e -> handleSubmit());
        formLayout.add(emailField, passwordField, submitButton, remindPasswordButton);

        // Ustawienie formularza na środku ekranu
        formLayout.setMaxWidth("300px"); // Maksymalna szerokość formularza
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP)); // Ustawienie, aby etykiety były nad polami
        formLayout.getStyle().set("margin", "auto"); // Wycentrowanie formularza

        setAlignItems(FlexComponent.Alignment.CENTER); // Wycentrowanie zawartości w pionie
        setSizeFull(); // Ustawienie pełnej dostępnej wysokości widoku
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER); // Wycentrowanie zawartości w poziomie
        add(header);
        add(formLayout);
        Button backButton = new Button("Back", event -> UI.getCurrent().navigate(""));
        add(backButton);

        // Ustawienie formularza na środku ekranu
        formLayout.setMaxWidth("300px"); // Maksymalna szerokość formularza
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP)); // Ustawienie, aby etykiety były nad polami
        formLayout.getStyle().set("margin", "auto"); // Wycentrowanie formularza
    }

    private void handleSubmit() {
        String emailValue = emailField.getValue();
        String passwordValue = passwordField.getValue();
        Response response = loginService.login(new LoginRequest(emailValue, passwordValue));
        if (response.isSuccess()) {
            Notification.show("Login complete with success", 5000, Notification.Position.MIDDLE);
            UI.getCurrent().navigate("logged");
        } else {
            Notification.show(response.error(), 5000, Notification.Position.MIDDLE);
        }
    }

    private void remindPassword() {
        UI.getCurrent().navigate("remindPassword");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        emailField.clear();
        passwordField.clear();
    }
}
