package com.example.psi.view;

import com.example.psi.request.VerifyCodeRequest;
import com.example.psi.response.VerifyCodeResponse;
import com.example.psi.service.VerifyCodeService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import org.springframework.stereotype.Component;

@Route("changepasswordcode")
@Component
public class InputChangePasswordCode extends VerticalLayout {

    private final VerifyCodeService verifyCodeService;
    private final TextField codeField = new TextField("Kod liczbowy");

    private final Button submitButton = new Button("Zatwierdź", event -> handleSubmit());
    public InputChangePasswordCode(VerifyCodeService verifyCodeService) {
        this.verifyCodeService = verifyCodeService;
        FormLayout formLayout = prepareFormLoginLayout();
        add(formLayout);
    }

    private FormLayout prepareFormLoginLayout() {
        H3 header = new H3("PSI - create new password");
        Binder<String> binder = new Binder<>();
        binder.forField(codeField)
                .withValidator(code -> code.matches("\\d*"), "Pole może zawierać tylko cyfry")
                .bind(code -> code, (code, value) -> codeField.setValue(value));

        codeField.addKeyPressListener(Key.ENTER, e -> handleSubmit());
        FormLayout formLayout = new FormLayout();
        formLayout.add(header, codeField, submitButton);

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

    private void handleSubmit() {
        String code = codeField.getValue();
        VerifyCodeRequest request = new VerifyCodeRequest(code);
        VerifyCodeResponse response = verifyCodeService.verifyCode(request);
        if (response.isSuccess()) {
            UI.getCurrent().navigate("newpassword");
        } else {
            Notification.show(response.error(), 5000, Notification.Position.MIDDLE);
        }
    }
}
