package com.example.psi.view;

import com.example.psi.response.RemindPasswordResponse;
import com.example.psi.service.RemindPasswordService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.router.Route;
import org.springframework.stereotype.Component;

@Route("remindPassword")
@Component
public class RemindPasswordView extends VerticalLayout {

    private final RemindPasswordService remindPasswordService;
    private final EmailField emailField = new EmailField("Email");
    private final Button remindPasswordButton;

    public RemindPasswordView(RemindPasswordService remindPasswordService) {
        this.remindPasswordService = remindPasswordService;
        remindPasswordButton = new Button("Przypomnij i zmień hasło", event -> remindPassword());
        createView();
    }

    private void createView() {
        H3 header = new H3("PSI - remind password");
        FormLayout formLayout = new FormLayout();
        formLayout.add(emailField, remindPasswordButton);

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

    private void remindPassword() {
        String email = emailField.getValue();
        RemindPasswordResponse resp = remindPasswordService.remindPassword(email);
        if(resp.isSuccess()) {
            UI.getCurrent().navigate("changepasswordcode");
        }else{
            Notification.show(resp.error(), 5000, Notification.Position.MIDDLE);
        }
    }
}
