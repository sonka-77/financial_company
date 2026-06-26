package com.company.view;

import com.company.controller.AuthController;
import com.company.controller.EnterpriseController;
import com.company.model.Enterprise;
import com.company.model.User;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;
public class LoginView {

    private final Stage stage;
    private final AuthController authController;
    private final EnterpriseController enterpriseController;

    public LoginView(Stage stage) {
        this.stage = stage;
        this.authController = new AuthController();
        this.enterpriseController = new EnterpriseController();
    }

    public void show() {
        stage.setTitle("Финансовые показатели — Вход");

        Text title = new Text("Финансовые показатели предприятий");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        Label loginLabel = new Label("Логин:");
        TextField loginField = new TextField();
        loginField.setPromptText("Введите логин");

        Label passLabel = new Label("Пароль:");
        PasswordField passField = new PasswordField();
        passField.setPromptText("Введите пароль");

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        Button loginBtn    = new Button("Войти");
        Button registerBtn = new Button("Регистрация");
        loginBtn.setDefaultButton(true);
        loginBtn.setPrefWidth(120);
        registerBtn.setPrefWidth(120);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(loginLabel, 0, 0); grid.add(loginField, 1, 0);
        grid.add(passLabel,  0, 1); grid.add(passField,  1, 1);

        HBox buttons = new HBox(10, loginBtn, registerBtn);
        buttons.setAlignment(Pos.CENTER);

        VBox root = new VBox(15, title, grid, errorLabel, buttons);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));

        loginBtn.setOnAction(e -> {
            String login    = loginField.getText().trim();
            String password = passField.getText();
            if (login.isEmpty() || password.isEmpty()) {
                errorLabel.setText("Заполните все поля.");
                return;
            }
            User user = authController.login(login, password);
            if (user == null) {
                errorLabel.setText("Неверный логин или пароль.");
                passField.clear();
                return;
            }

            // админ - сразу в главное окно
            if (user.isAdmin()) {
                openMain(user);
                return;
            }

            // обычный пользователь уже выбирал предприятие -- сразу в главное окно
            if (user.getEnterpriseId() != null) {
                openMain(user);
                return;
            }

            // первый вход пользователя и предприятие ещё не выбрано -- показываем выпадающий список
            showEnterpriseChooser(user);
        });

        registerBtn.setOnAction(e -> new RegisterView(stage, this).show());

        stage.setScene(new Scene(root, 400, 280));
        stage.setResizable(false);
        stage.show();
    }


      //экран выбора предприятия при первом входе.
       //после выбора id_enterprise сохраняется в бд,

    private void showEnterpriseChooser(User user) {
        stage.setTitle("Финансовые показатели — Выбор предприятия");

        Text title = new Text("Выберите ваше предприятие");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 15));

        Label hint = new Label("Это нужно сделать один раз.\nПри следующем входе вы попадёте в него автоматически.");
        hint.setWrapText(true);
        hint.setStyle("-fx-text-fill: #555; -fx-font-size: 11;");

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        ComboBox<Enterprise> combo = new ComboBox<>();
        combo.setPromptText("— Выберите предприятие —");
        combo.setPrefWidth(300);

        try {
            List<Enterprise> enterprises = enterpriseController.getAllEnterprises();
            if (enterprises.isEmpty()) {
                errorLabel.setText("В системе нет предприятий. Обратитесь к администратору.");
            } else {
                combo.setItems(FXCollections.observableArrayList(enterprises));
            }
        } catch (Exception ex) {
            errorLabel.setText("Ошибка загрузки: " + ex.getMessage());
        }

        Button confirmBtn = new Button("Войти в предприятие");
        confirmBtn.setPrefWidth(200);
        confirmBtn.setDefaultButton(true);

        confirmBtn.setOnAction(e -> {
            Enterprise chosen = combo.getValue();
            if (chosen == null) {
                errorLabel.setText("Пожалуйста, выберите предприятие из списка.");
                return;
            }
            // сохраняем выбор в бд
            try {
                authController.assignEnterprise(user.getId(), chosen.getId());
                user.setEnterpriseId(chosen.getId()); // обновляем объект в памяти
                openMain(user);
            } catch (Exception ex) {
                errorLabel.setText("Не удалось сохранить выбор: " + ex.getMessage());
            }
        });

        VBox root = new VBox(15, title, hint, combo, errorLabel, confirmBtn);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));

        stage.setScene(new Scene(root, 420, 280));
        stage.setResizable(false);
        stage.show();
    }

    private void openMain(User user) {
        new MainView(stage, user).show();
    }
}
