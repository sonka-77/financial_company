package com.company.view;

import com.company.model.User;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class MainView {

    private final Stage stage;
    private final User currentUser;

    public MainView(Stage stage, User currentUser) {
        this.stage = stage;
        this.currentUser = currentUser;
    }

    public void show() {
        stage.setTitle("Финансовые показатели — " + currentUser.getLogin() +
                       " (" + currentUser.getRole() + ")");

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // вкладка "Показатели предприятий" — для всех
        Tab indicatorsTab = new Tab("Показатели");
        IndicatorsView indicatorsView = new IndicatorsView(currentUser);
        indicatorsTab.setContent(indicatorsView.getContent());

        // вкладка "Предприятия" — только для админа
        Tab enterprisesTab = new Tab("Предприятия");
        EnterprisesView enterprisesView = new EnterprisesView(currentUser);
        enterprisesTab.setContent(enterprisesView.getContent());

        // вкладка "Пользователи" — только для админа
        Tab usersTab = new Tab("Пользователи");
        UsersView usersView = new UsersView(currentUser);
        usersTab.setContent(usersView.getContent());

        // вкладка "Мой профиль" — для всех
        Tab profileTab = new Tab("Мой профиль");
        ProfileView profileView = new ProfileView(currentUser, stage);
        profileTab.setContent(profileView.getContent());


        tabPane.getTabs().add(indicatorsTab);

        if (currentUser.isAdmin()) {
            tabPane.getTabs().add(enterprisesTab);
            tabPane.getTabs().add(usersTab);
        }

        tabPane.getTabs().add(profileTab);


        Label statusLabel = new Label("Вы вошли как: " + currentUser.getLogin() +
                                      " | Роль: " + currentUser.getRole());
        statusLabel.setStyle("-fx-padding: 4; -fx-text-fill: #555;");

        Button logoutBtn = new Button("Выйти");
        logoutBtn.setOnAction(e -> {
            LoginView loginView = new LoginView(stage);
            loginView.show();
        });

        HBox statusBar = new HBox(10, statusLabel, logoutBtn);
        statusBar.setPadding(new Insets(5, 10, 5, 10));
        statusBar.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #ccc; -fx-border-width: 1 0 0 0;");

        BorderPane root = new BorderPane();
        root.setCenter(tabPane);
        root.setBottom(statusBar);

        Scene scene = new Scene(root, 900, 600);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }
}
