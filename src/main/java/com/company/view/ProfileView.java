package com.company.view;

import com.company.controller.AuthController;
import com.company.controller.Validator;
import com.company.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ProfileView {

    private final User currentUser;
    private final Stage stage;
    private final AuthController controller;

    public ProfileView(User currentUser, Stage stage) {
        this.currentUser = currentUser;
        this.stage = stage;
        this.controller = new AuthController();
    }

    public VBox getContent() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(30));
        root.setMaxWidth(400);
        root.setAlignment(Pos.TOP_LEFT);

        Label title = new Label("Мой профиль");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");


        Label infoLabel = new Label("Текущий логин: " + currentUser.getLogin() +
                                    "\nРоль: " + currentUser.getRole());
        infoLabel.setStyle("-fx-text-fill: #333;");

        Separator sep = new Separator();

        Label changeLabel = new Label("Изменить данные:");
        changeLabel.setStyle("-fx-font-weight: bold;");

        TextField newLoginField = new TextField(currentUser.getLogin());
        newLoginField.setPromptText("Новый логин");

        PasswordField newPassField = new PasswordField();
        newPassField.setPromptText("Новый пароль");

        PasswordField confirmPassField = new PasswordField();
        confirmPassField.setPromptText("Подтвердите новый пароль");

        Label hintLabel = new Label(Validator.getPasswordRequirements());
        hintLabel.setStyle("-fx-text-fill: gray; -fx-font-size: 11;");

        Label resultLabel = new Label();

        Button saveBtn = new Button("Сохранить изменения");
        saveBtn.setPrefWidth(200);

        saveBtn.setOnAction(e -> {
            String newLogin = newLoginField.getText().trim();
            String newPass = newPassField.getText();
            String confirmPass = confirmPassField.getText();

            if (newLogin.isEmpty()) {
                resultLabel.setStyle("-fx-text-fill: red;");
                resultLabel.setText("Логин не может быть пустым.");
                return;
            }
            if (!newPass.equals(confirmPass)) {
                resultLabel.setStyle("-fx-text-fill: red;");
                resultLabel.setText("Пароли не совпадают.");
                return;
            }
            if (!Validator.isValidPassword(newPass)) {
                resultLabel.setStyle("-fx-text-fill: red;");
                resultLabel.setText("Пароль не соответствует требованиям.");
                return;
            }
            try {
                boolean ok = controller.updateUser(currentUser.getId(), newLogin, newPass);
                if (ok) {
                    currentUser.setLogin(newLogin);
                    stage.setTitle("Финансовые показатели — " + newLogin +
                                   " (" + currentUser.getRole() + ")");
                    resultLabel.setStyle("-fx-text-fill: green;");
                    resultLabel.setText("Данные успешно обновлены!");
                    infoLabel.setText("Текущий логин: " + newLogin +
                                      "\nРоль: " + currentUser.getRole());
                    newPassField.clear();
                    confirmPassField.clear();
                } else {
                    resultLabel.setStyle("-fx-text-fill: red;");
                    resultLabel.setText("Не удалось сохранить изменения.");
                }
            } catch (Exception ex) {
                resultLabel.setStyle("-fx-text-fill: red;");
                resultLabel.setText("Ошибка: " + ex.getMessage());
            }
        });

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Логин:"), 0, 0);
        grid.add(newLoginField, 1, 0);
        grid.add(new Label("Новый пароль:"), 0, 1);
        grid.add(newPassField, 1, 1);
        grid.add(new Label("Подтверждение:"), 0, 2);
        grid.add(confirmPassField, 1, 2);

        root.getChildren().addAll(title, infoLabel, sep, changeLabel, grid, hintLabel, saveBtn, resultLabel);
        return root;
    }
}
