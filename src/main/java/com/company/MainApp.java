package com.company;

import com.company.db.DatabaseConnection;
import com.company.view.LoginView;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Главный класс приложения — точка входа.
 */
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        LoginView loginView = new LoginView(primaryStage);
        loginView.show();
    }

    @Override
    public void stop() {
        // Закрываем соединение с БД при выходе из приложения
        try {
            DatabaseConnection instance = DatabaseConnection.getInstance();
            instance.closeConnection();
        } catch (Exception e) {
            System.err.println("Ошибка при закрытии соединения: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
