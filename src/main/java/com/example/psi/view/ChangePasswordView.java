package com.example.psi.view;

import com.example.psi.request.ChangePasswordRequest;
import com.example.psi.request.LoginRequest;
import com.example.psi.response.ChangePasswordResponse;
import com.example.psi.response.Response;
import com.example.psi.service.ChangePasswordService;
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

@Route("changepassword")
@Component
public class ChangePasswordView extends VerticalLayout {

    private final ChangePasswordService changePasswordService;
    private PasswordField oldPasswordFiled = new PasswordField("Stare Hasło");
    private PasswordField newpasswordField = new PasswordField("Nowe Hasło");
    private Button backButton = new Button("Back", event -> UI.getCurrent().navigate("logged"));
    private Button submitButton = new Button("Zmień hasło", event -> handleSubmit());


    public ChangePasswordView(ChangePasswordService changePasswordService) {
        oldPasswordFiled.clear();
        newpasswordField.clear();
        this.changePasswordService = changePasswordService;
        createView();
    }

    private void createView() {
        H3 header = new H3("PSI - change password");
        FormLayout formLayout = new FormLayout();
        oldPasswordFiled.addKeyPressListener(Key.ENTER, e -> handleSubmit());
        newpasswordField.addKeyPressListener(Key.ENTER, e -> handleSubmit());
        formLayout.add(header, oldPasswordFiled, newpasswordField, submitButton);
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
        add(formLayout);
        add(backButton);
    }

    private void handleSubmit() {
        String oldPassword = oldPasswordFiled.getValue();
        String newPassword = newpasswordField.getValue();
        ChangePasswordRequest request = new ChangePasswordRequest(oldPassword, newPassword);
        ChangePasswordResponse response = changePasswordService.changePassword(request);
        if (response.isSuccess()) {
            Notification.show("Change password complete with success", 5000, Notification.Position.MIDDLE);
            UI.getCurrent().navigate("logged");
        } else {
            Notification.show(response.error(), 5000, Notification.Position.MIDDLE);
        }
    }
}
