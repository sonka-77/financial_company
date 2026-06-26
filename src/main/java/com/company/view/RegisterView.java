package com.company.view;

import com.company.controller.AuthController;
import com.company.controller.Validator;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

// регистрация новых польщователей
public class RegisterView {

    private final Stage stage;
    private final LoginView loginView;
    private final AuthController authController;

    public RegisterView(Stage stage, LoginView loginView) {
        this.stage = stage;
        this.loginView = loginView;
        this.authController = new AuthController();
    }

    public void show() {
        stage.setTitle("Регистрация");

        Text title = new Text("Регистрация нового пользователя");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        Label loginLabel = new Label("Логин:");
        TextField loginField = new TextField();
        loginField.setPromptText("Придумайте логин");

        Label passLabel = new Label("Пароль:");
        PasswordField passField = new PasswordField();
        passField.setPromptText("Придумайте пароль");

        Label pass2Label = new Label("Повтор пароля:");
        PasswordField pass2Field = new PasswordField();
        pass2Field.setPromptText("Повторите пароль");

        // вывод требований к паролю
        Label hint = new Label(Validator.getPasswordRequirements());
        hint.setStyle("-fx-text-fill: gray; -fx-font-size: 11;");

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        Button registerBtn = new Button("Зарегистрироваться");
        Button backBtn = new Button("Назад");
        registerBtn.setPrefWidth(160);
        backBtn.setPrefWidth(100);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(loginLabel, 0, 0);
        grid.add(loginField, 1, 0);
        grid.add(passLabel, 0, 1);
        grid.add(passField, 1, 1);
        grid.add(pass2Label, 0, 2);
        grid.add(pass2Field, 1, 2);

        HBox buttons = new HBox(10, registerBtn, backBtn);
        buttons.setAlignment(Pos.CENTER);

        VBox root = new VBox(12, title, grid, hint, errorLabel, buttons);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(25));

        registerBtn.setOnAction(e -> {
            String login = loginField.getText().trim();
            String pass = passField.getText();
            String pass2 = pass2Field.getText();

            if (login.isEmpty() || pass.isEmpty()) {
                errorLabel.setText("Заполните все поля.");
                return;
            }
            if (!pass.equals(pass2)) {
                errorLabel.setText("Пароли не совпадают.");
                return;
            }
            if (!Validator.isValidPassword(pass)) {
                errorLabel.setText("Пароль не соответствует требованиям.");
                return;
            }

            String result = authController.register(login, pass);
            if (result == null) {
                showInfo("Регистрация успешна! Теперь войдите в систему.");
                loginView.show();
            } else {
                errorLabel.setText(result);
            }
        });

        backBtn.setOnAction(e -> loginView.show());

        Scene scene = new Scene(root, 420, 360);
        stage.setScene(scene);
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle("Успех");
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
