package com.company.view;

import com.company.controller.AuthController;
import com.company.controller.EnterpriseController;
import com.company.model.Enterprise;
import com.company.model.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;

 //вкладка «Пользователи» для админа

public class UsersView {

    private final User currentUser;
    private final AuthController authController;
    private final EnterpriseController enterpriseController;
    private TableView<User> table;

    public UsersView(User currentUser) {
        this.currentUser         = currentUser;
        this.authController      = new AuthController();
        this.enterpriseController = new EnterpriseController();
    }

    public VBox getContent() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));

        Label title = new Label("Управление пользователями");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        Label info = new Label(
            "Обычный пользователь сам выбирает предприятие при первом входе. " +
            "Здесь вы можете изменить или сбросить этот выбор."
        );
        info.setWrapText(true);
        info.setStyle("-fx-text-fill: #555; -fx-font-size: 11;");

        // ── Таблица ──────────────────────────────────────────────
        table = new TableView<>();
        table.setPlaceholder(new Label("Нет пользователей"));
        VBox.setVgrow(table, Priority.ALWAYS);

        TableColumn<User, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(d ->
            new SimpleStringProperty(String.valueOf(d.getValue().getId())));
        colId.setPrefWidth(50);

        TableColumn<User, String> colLogin = new TableColumn<>("Логин");
        colLogin.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue().getLogin()));
        colLogin.setPrefWidth(180);

        TableColumn<User, String> colRole = new TableColumn<>("Роль");
        colRole.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue().getRole()));
        colRole.setPrefWidth(100);

        TableColumn<User, String> colEnterprise = new TableColumn<>("Предприятие");
        colEnterprise.setCellValueFactory(d -> {
            if (d.getValue().isAdmin())
                return new SimpleStringProperty("все предприятия");
            Integer eid = d.getValue().getEnterpriseId();
            if (eid == null)
                return new SimpleStringProperty("не выбрано (выберет при входе)");
            // Ищем название предприятия
            try {
                List<Enterprise> all = enterpriseController.getAllEnterprises();
                return new SimpleStringProperty(
                    all.stream()
                       .filter(en -> en.getId() == eid)
                       .map(Enterprise::getName)
                       .findFirst()
                       .orElse("ID: " + eid)
                );
            } catch (Exception e) {
                return new SimpleStringProperty("ID: " + eid);
            }
        });
        colEnterprise.setPrefWidth(260);

        table.getColumns().addAll(colId, colLogin, colRole, colEnterprise);

        // ── Кнопки ───────────────────────────────────────────────
        Button changeBtn  = new Button("Изменить предприятие");
        Button resetBtn   = new Button("Сбросить предприятие");
        Button deleteBtn  = new Button("Удалить пользователя");
        Button refreshBtn = new Button("Обновить");

        changeBtn.setTooltip(new Tooltip(
            "Назначить пользователю другое предприятие.\n" +
            "При следующем входе он попадёт в него автоматически."));
        resetBtn.setTooltip(new Tooltip(
            "Убрать привязку к предприятию.\n" +
            "При следующем входе пользователь сам выберет предприятие."));

        changeBtn.setOnAction(e  -> onChangeEnterprise());
        resetBtn.setOnAction(e   -> onResetEnterprise());
        deleteBtn.setOnAction(e  -> onDelete());
        refreshBtn.setOnAction(e -> loadData());

        HBox buttons = new HBox(10, changeBtn, resetBtn, deleteBtn, refreshBtn);

        root.getChildren().addAll(title, info, table, buttons);
        loadData();
        return root;
    }

    // ── Загрузка списка пользователей ────────────────────────────
    private void loadData() {
        try {
            table.setItems(FXCollections.observableArrayList(
                authController.getAllUsers()
            ));
        } catch (Exception e) {
            showError("Ошибка загрузки: " + e.getMessage());
        }
    }

    // изменить предприятие пользователя для админа
    private void onChangeEnterprise() {
        User selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) { showError("Выберите пользователя."); return; }
        if (selected.isAdmin()) {
            showError("У администратора нет привязки к предприятию — он видит все.");
            return;
        }

        try {
            List<Enterprise> enterprises = enterpriseController.getAllEnterprises();
            if (enterprises.isEmpty()) {
                showError("В системе нет предприятий. Сначала добавьте предприятие.");
                return;
            }

            // выпадающий список
            Dialog<Enterprise> dialog = new Dialog<>();
            dialog.setTitle("Изменить предприятие");
            dialog.setHeaderText(
                "Пользователь: " + selected.getLogin() + "\n" +
                "Выберите новое предприятие:"
            );

            ComboBox<Enterprise> combo = new ComboBox<>(
                FXCollections.observableArrayList(enterprises));
            combo.setPrefWidth(280);
            if (selected.getEnterpriseId() != null) {
                enterprises.stream()
                    .filter(en -> en.getId() == selected.getEnterpriseId())
                    .findFirst()
                    .ifPresent(combo::setValue);
            }

            VBox content = new VBox(8, new Label("Предприятие:"), combo);
            content.setPadding(new Insets(15));
            dialog.getDialogPane().setContent(content);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            dialog.setResultConverter(btn -> btn == ButtonType.OK ? combo.getValue() : null);

            dialog.showAndWait().ifPresent(enterprise -> {
                if (enterprise == null) { showError("Выберите предприятие из списка."); return; }
                try {
                    authController.assignEnterprise(selected.getId(), enterprise.getId());
                    loadData();
                    showInfo(
                        "Предприятие изменено.\n" +
                        "Пользователь «" + selected.getLogin() + "» при следующем входе\n" +
                        "попадёт в «" + enterprise.getName() + "» автоматически."
                    );
                } catch (Exception ex) {
                    showError("Ошибка: " + ex.getMessage());
                }
            });

        } catch (Exception e) {
            showError("Ошибка загрузки предприятий: " + e.getMessage());
        }
    }

    // сбросить предприятие
    private void onResetEnterprise() {
        User selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) { showError("Выберите пользователя."); return; }
        if (selected.isAdmin()) {
            showError("Администратору нельзя сбросить предприятие.");
            return;
        }
        if (selected.getEnterpriseId() == null) {
            showInfo("У этого пользователя предприятие и так не выбрано.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
            "Сбросить предприятие пользователя «" + selected.getLogin() + "»?\n\n" +
            "При следующем входе ему снова покажется экран выбора предприятия.",
            ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText(null);
        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                try {
                    authController.resetEnterprise(selected.getId());
                    loadData();
                    showInfo("Предприятие сброшено. При следующем входе пользователь выберет его сам.");
                } catch (Exception e) {
                    showError("Ошибка: " + e.getMessage());
                }
            }
        });
    }

    // удалить пользователя
    private void onDelete() {
        User selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) { showError("Выберите пользователя."); return; }
        if (selected.getId() == currentUser.getId()) {
            showError("Нельзя удалить самого себя.");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
            "Удалить пользователя «" + selected.getLogin() + "»?",
            ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText(null);
        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                try {
                    authController.deleteUser(selected.getId());
                    loadData();
                } catch (Exception e) {
                    showError("Ошибка: " + e.getMessage());
                }
            }
        });
    }

    private void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        a.setHeaderText(null); a.showAndWait();
    }

    private void showInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.setHeaderText(null); a.showAndWait();
    }
}
