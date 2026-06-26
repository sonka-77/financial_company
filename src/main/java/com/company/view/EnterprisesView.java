package com.company.view;

import com.company.controller.EnterpriseController;
import com.company.model.Enterprise;
import com.company.model.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;

//для админа
public class EnterprisesView {

    private final User currentUser;
    private final EnterpriseController controller;
    private TableView<Enterprise> table;

    public EnterprisesView(User currentUser) {
        this.currentUser = currentUser;
        this.controller = new EnterpriseController();
    }

    public VBox getContent() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));

        Label title = new Label("Управление предприятиями");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        table = new TableView<>();
        table.setPlaceholder(new Label("Нет данных"));
        VBox.setVgrow(table, Priority.ALWAYS);

        TableColumn<Enterprise, String> colName = new TableColumn<>("Название");
        colName.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getName()));
        colName.setPrefWidth(200);

        TableColumn<Enterprise, String> colReq = new TableColumn<>("Реквизиты");
        colReq.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getRequisites()));
        colReq.setPrefWidth(180);

        TableColumn<Enterprise, String> colPhone = new TableColumn<>("Телефон");
        colPhone.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getPhone()));
        colPhone.setPrefWidth(130);

        TableColumn<Enterprise, String> colContact = new TableColumn<>("Контактное лицо");
        colContact.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getContactPerson()));
        colContact.setPrefWidth(200);

        table.getColumns().addAll(colName, colReq, colPhone, colContact);

        Button addBtn = new Button("Добавить");
        Button editBtn = new Button("Редактировать");
        Button deleteBtn = new Button("Удалить");
        Button refreshBtn = new Button("Обновить");

        addBtn.setOnAction(e -> onAdd());
        editBtn.setOnAction(e -> onEdit());
        deleteBtn.setOnAction(e -> onDelete());
        refreshBtn.setOnAction(e -> loadData());

        HBox buttons = new HBox(10, addBtn, editBtn, deleteBtn, refreshBtn);

        root.getChildren().addAll(title, table, buttons);
        loadData();
        return root;
    }

    private void loadData() {
        try {
            List<Enterprise> list = controller.getAllEnterprises();
            table.setItems(FXCollections.observableArrayList(list));
        } catch (Exception e) {
            showError("Ошибка загрузки: " + e.getMessage());
        }
    }

    private void onAdd() {
        showForm(null);
    }

    private void onEdit() {
        Enterprise selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) { showError("Выберите предприятие."); return; }
        showForm(selected);
    }

    private void showForm(Enterprise enterprise) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(enterprise == null ? "Добавить предприятие" : "Редактировать предприятие");

        TextField nameField = new TextField(enterprise != null ? enterprise.getName() : "");
        TextField reqField = new TextField(enterprise != null ? enterprise.getRequisites() : "");
        TextField phoneField = new TextField(enterprise != null ? enterprise.getPhone() : "");
        TextField contactField = new TextField(enterprise != null ? enterprise.getContactPerson() : "");

        nameField.setPromptText("Название предприятия");
        reqField.setPromptText("Реквизиты (ИНН/БИН)");
        phoneField.setPromptText("+7 xxx xxx xx xx");
        contactField.setPromptText("ФИО контактного лица");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15));
        grid.add(new Label("Название:"), 0, 0);      grid.add(nameField, 1, 0);
        grid.add(new Label("Реквизиты:"), 0, 1);     grid.add(reqField, 1, 1);
        grid.add(new Label("Телефон:"), 0, 2);       grid.add(phoneField, 1, 2);
        grid.add(new Label("Контакт:"), 0, 3);       grid.add(contactField, 1, 3);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.OK) {
                String name = nameField.getText().trim();
                String req = reqField.getText().trim();
                String phone = phoneField.getText().trim();
                String contact = contactField.getText().trim();
                if (name.isEmpty() || req.isEmpty() || phone.isEmpty()) {
                    showError("Заполните обязательные поля: Название, Реквизиты, Телефон.");
                    return;
                }
                try {
                    if (enterprise == null) {
                        controller.addEnterprise(new Enterprise(0, name, req, phone, contact));
                    } else {
                        enterprise.setName(name);
                        enterprise.setRequisites(req);
                        enterprise.setPhone(phone);
                        enterprise.setContactPerson(contact);
                        controller.updateEnterprise(enterprise);
                    }
                    loadData();
                } catch (Exception e) {
                    showError("Ошибка сохранения: " + e.getMessage());
                }
            }
        });
    }

    private void onDelete() {
        Enterprise selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) { showError("Выберите предприятие."); return; }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Удалить предприятие \"" + selected.getName() + "\"?", ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText(null);
        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                try {
                    controller.deleteEnterprise(selected.getId());
                    loadData();
                } catch (Exception e) {
                    showError("Ошибка удаления: " + e.getMessage());
                }
            }
        });
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK).showAndWait();
    }
}
