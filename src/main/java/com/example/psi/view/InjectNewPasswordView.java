package com.example.psi.view;

import com.example.psi.request.ChangePasswordRequest;
import com.example.psi.request.CreateNewPasswordRequest;
import com.example.psi.response.ChangePasswordResponse;
import com.example.psi.response.CreateNewPasswordResponse;
import com.example.psi.service.ChangePasswordService;
import com.example.psi.service.CreateNewPasswordService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.Route;
import org.springframework.stereotype.Component;

@Route("newpassword")
@Component
public class InjectNewPasswordView extends VerticalLayout {

    private final CreateNewPasswordService createNewPasswordService;
    private PasswordField newpasswordField = new PasswordField("Nowe Hasło");

    private Button submitButton = new Button("Zmień hasło", event -> handleSubmit());


    public InjectNewPasswordView(CreateNewPasswordService createNewPasswordService) {
        this.createNewPasswordService = createNewPasswordService;
        createView();
    }

    private void createView() {
        H3 header = new H3("PSI - change password");
        FormLayout formLayout = new FormLayout();
        newpasswordField.addKeyPressListener(Key.ENTER, e -> handleSubmit());
        formLayout.add(header, newpasswordField, submitButton);
        // Ustawienie formularza na środku ekranu
        formLayout.setMaxWidth("300px"); // Maksymalna szerokość formularza
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP)); // Ustawienie, aby etykiety były nad polami
        formLayout.getStyle().set("margin", "auto"); // Wycentrowanie formularza

        setAlignItems(Alignment.CENTER); // Wycentrowanie zawartości w pionie
        setSizeFull(); // Ustawienie pełnej dostępnej wysokości widoku
        setJustifyContentMode(JustifyContentMode.CENTER);

        formLayout.setMaxWidth("500px"); // Maksymalna szerokość formularza
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP)); // Ustawienie, aby etykiety były nad polami
        formLayout.getStyle().set("margin", "auto"); // Wycentrowanie formularza
        add(formLayout);
    }

    private void handleSubmit() {
        String newPassword = newpasswordField.getValue();
        CreateNewPasswordRequest request = new CreateNewPasswordRequest(newPassword);
        CreateNewPasswordResponse response = createNewPasswordService.changePassword(request);
        if (response.isSuccess()) {
            Notification.show("Change password complete with success", 5000, Notification.Position.MIDDLE);
            UI.getCurrent().navigate("login");
        } else {
            Notification.show(response.error(), 5000, Notification.Position.MIDDLE);
        }
    }
}
