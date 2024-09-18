package com.example.psi.view;

import com.example.psi.model.InvalidLogin;
import com.example.psi.model.LoggedUser;
import com.example.psi.model.Session;
import com.example.psi.service.LoggedUserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import org.springframework.stereotype.Component;

import java.util.List;

@Route("logged")
@Component
public class LoggedUserView extends VerticalLayout implements BeforeEnterObserver {

    private final LoggedUserService loggedUserService;
    private Grid<LoggedUser> loggedUserGrid;
    private Grid<Session> sessionGrid;
    private Grid<InvalidLogin> invalidLoginGrid;


    public LoggedUserView(LoggedUserService loggedUserService) {
        this.loggedUserService = loggedUserService;
        // Pasek nawigacyjny na górze
        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setSpacing(true);

        // Dodanie przycisków do paska nawigacyjnego
        Button changePasswordButton = new Button("Zmień hasło", e -> changePassword());
        Button logoutButton = new Button("Wyloguj", e -> logout());

        // Dodanie przycisków do prawej strony
        header.add(new Div(), changePasswordButton, logoutButton);
        header.expand(new Div()); // To sprawia, że przyciski są po prawej

        // Dodanie paska do głównego layoutu
        add(header);

        LoggedUser loggedUser = loggedUserService.getLoggedUser();

        // Tabela z polami edycji na środku
        loggedUserGrid = new Grid<>(LoggedUser.class, false);

        // Kolumny do edycji (przykład z dwoma kolumnami: "name" i "email")
        loggedUserGrid.addColumn(LoggedUser::getEmail).setHeader("Email").setEditorComponent(new TextField());
        loggedUserGrid.addColumn(LoggedUser::getTimeOfLastChangedPassword).setHeader("timeOfLastChangedPassword").setEditorComponent(new TextField());

        // Pobieranie danych z bazy (przykład)


        sessionGrid = new Grid<>(Session.class, false);
        sessionGrid.addColumn(Session::getId).setHeader("ID of session");
        sessionGrid.addColumn(Session::getStart).setHeader("Start session time");
        sessionGrid.addColumn(Session::getStop).setHeader("Stop session time");


        invalidLoginGrid = new Grid<>(InvalidLogin.class, false);
        invalidLoginGrid.addColumn(InvalidLogin::getId).setHeader("ID");
        invalidLoginGrid.addColumn(InvalidLogin::getLocalDateTime).setHeader("Czas zajścia nieprawidłowego logowania");

        // Dodanie siatki do głównego layoutu
        add(loggedUserGrid, sessionGrid, invalidLoginGrid);
    }

    private void changePassword() {
        // Logika zmiany hasła
        UI.getCurrent().navigate("changepassword");
    }

    private void logout() {
        loggedUserService.logout();
        Notification.show("Wylogowano");
        UI.getCurrent().navigate("login");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        LoggedUser loggedUser = loggedUserService.getLoggedUser();
        loggedUserGrid.setItems(new ListDataProvider<>(List.of(loggedUser)));
        sessionGrid.setItems(loggedUser.getSessions());
        invalidLoginGrid.setItems(loggedUser.getInvalidLogins());
    }
}
