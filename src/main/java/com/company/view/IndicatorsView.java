package com.company.view;

import com.company.controller.IndicatorController;
import com.company.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.math.BigDecimal;
import java.util.List;

public class IndicatorsView {

    private final User currentUser;
    private final IndicatorController controller;

    private ComboBox<Enterprise> enterpriseCombo;
    private TableView<IndicatorValue> table;

    public IndicatorsView(User currentUser) {
        this.currentUser = currentUser;
        this.controller = new IndicatorController();
    }

    public VBox getContent() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));

        if (currentUser.isAdmin()) {
            TabPane innerTabs = new TabPane();
            innerTabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
            Tab valuesTab = new Tab("Значения по предприятиям");
            valuesTab.setContent(buildValuesPane());
            Tab indicatorsTab = new Tab("Справочник показателей");
            indicatorsTab.setContent(buildIndicatorsPane());
            innerTabs.getTabs().addAll(valuesTab, indicatorsTab);
            VBox.setVgrow(innerTabs, Priority.ALWAYS);
            root.getChildren().add(innerTabs);
        } else {
            root.getChildren().add(buildValuesPane());
        }
        return root;
    }

    private VBox buildValuesPane() {
        VBox pane = new VBox(10);
        pane.setPadding(new Insets(10));

        Label enterpriseLabel = new Label("Предприятие:");
        enterpriseCombo = new ComboBox<>();
        enterpriseCombo.setPrefWidth(300);
        HBox topBar = new HBox(10, enterpriseLabel, enterpriseCombo);

        table = new TableView<>();
        table.setPlaceholder(new Label("Выберите предприятие"));
        VBox.setVgrow(table, Priority.ALWAYS);

        TableColumn<IndicatorValue, String> colInd = new TableColumn<>("Показатель");
        colInd.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getIndicatorName()));
        colInd.setPrefWidth(250);

        TableColumn<IndicatorValue, String> colPer = new TableColumn<>("Период");
        colPer.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getQuarter()));
        colPer.setPrefWidth(100);

        TableColumn<IndicatorValue, String> colVal = new TableColumn<>("Значение");
        colVal.setCellValueFactory(d -> {
            BigDecimal v = d.getValue().getValue();
            return new SimpleStringProperty(v != null ? v.toPlainString() : "—");
        });
        colVal.setPrefWidth(150);

        TableColumn<IndicatorValue, String> colCur = new TableColumn<>("Валюта");
        colCur.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCurrencyType()));
        colCur.setPrefWidth(80);
        

        table.getColumns().addAll(colInd, colPer, colVal, colCur);

        Button addBtn    = new Button("Добавить запись");
        Button editBtn   = new Button("Редактировать значение");
        Button deleteBtn = new Button("Удалить запись");
        Button refreshBtn = new Button("Обновить");

        if (!currentUser.isAdmin()) {
            addBtn.setDisable(true);
            deleteBtn.setDisable(true);
        }

        addBtn.setOnAction(e -> onAdd());
        editBtn.setOnAction(e -> onEdit());
        deleteBtn.setOnAction(e -> onDelete());
        refreshBtn.setOnAction(e -> loadTableData());

        enterpriseCombo.setOnAction(e -> {
            if (enterpriseCombo.getValue() != null) {
                loadTableData();
            }
        });

        loadEnterprises();

        HBox buttons = new HBox(10, addBtn, editBtn, deleteBtn, refreshBtn);
        pane.getChildren().addAll(topBar, table, buttons);
        return pane;
    }

    private void loadEnterprises() {
        try {
            List<Enterprise> enterprises = currentUser.isAdmin()
                    ? controller.getAllEnterprises()
                    : controller.getEnterprisesByUser(currentUser.getId());

            enterpriseCombo.setItems(FXCollections.observableArrayList(enterprises));

            if (!enterprises.isEmpty()) {
                if (!currentUser.isAdmin()) {
                    enterpriseCombo.setValue(enterprises.get(0));
                    enterpriseCombo.setDisable(true);
                    loadTableData();
                } else if (enterprises.size() == 1) {
                    enterpriseCombo.setValue(enterprises.get(0));
                    loadTableData();
                }
            }
        } catch (Exception e) {
            showError("Ошибка загрузки предприятий: " + e.getMessage());
        }
    }

    private void loadTableData() {
        Enterprise selected = enterpriseCombo.getValue();
        if (selected == null) return;
        try {
            List<IndicatorValue> values = controller.getValuesByEnterprise(selected.getId());
            table.setItems(FXCollections.observableArrayList(values));
        } catch (Exception e) {
            showError("Ошибка загрузки данных: " + e.getMessage());
        }
    }

    private void onAdd() {
        Enterprise enterprise = enterpriseCombo.getValue();
        if (enterprise == null) { showError("Сначала выберите предприятие."); return; }
        try {
            List<Indicator> indicators = controller.getAllIndicators();
            List<Period> periods = controller.getAllPeriods();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Добавить запись показателя");

            ComboBox<Indicator> indCombo = new ComboBox<>(FXCollections.observableArrayList(indicators));
            ComboBox<Period> periodCombo = new ComboBox<>(FXCollections.observableArrayList(periods));
            TextField valueField = new TextField();
            valueField.setPromptText("Числовое значение");

            GridPane grid = new GridPane();
            grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(15));
            grid.add(new Label("Показатель:"), 0, 0); grid.add(indCombo, 1, 0);
            grid.add(new Label("Период:"),     0, 1); grid.add(periodCombo, 1, 1);
            grid.add(new Label("Значение:"),   0, 2); grid.add(valueField, 1, 2);

            dialog.getDialogPane().setContent(grid);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            dialog.showAndWait().ifPresent(btn -> {
                if (btn == ButtonType.OK) {
                    Indicator ind = indCombo.getValue();
                    Period period = periodCombo.getValue();
                    String valStr = valueField.getText().trim();
                    if (ind == null || period == null || valStr.isEmpty()) {
                        showError("Заполните все поля."); return;
                    }
                    try {
                        BigDecimal value = new BigDecimal(valStr);
                        controller.addValue(enterprise.getId(), ind.getId(), period.getId(), value);
                        loadTableData();
                    } catch (NumberFormatException ex) {
                        showError("Введите корректное числовое значение.");
                    } catch (Exception ex) {
                        showError("Ошибка: " + ex.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            showError("Ошибка: " + e.getMessage());
        }
    }

    private void onEdit() {
        IndicatorValue selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) { showError("Выберите строку для редактирования."); return; }

        TextInputDialog dialog = new TextInputDialog(
                selected.getValue() != null ? selected.getValue().toPlainString() : "");
        dialog.setTitle("Редактировать значение");
        dialog.setHeaderText("Показатель: " + selected.getIndicatorName() +
                             "\nПериод: " + selected.getQuarter());
        dialog.setContentText("Новое значение:");

        dialog.showAndWait().ifPresent(val -> {
            try {
                BigDecimal newValue = new BigDecimal(val.trim());
                // передача контроллеру составного ключа
                controller.updateValue(
                    selected.getEnterpriseId(),
                    selected.getPeriodId(),
                    selected.getIndicatorId(),
                    selected.getCurrencyId(),
                    newValue
                );
                loadTableData();
            } catch (NumberFormatException ex) {
                showError("Введите корректное числовое значение.");
            } catch (Exception ex) {
                showError("Ошибка: " + ex.getMessage());
            }
        });
    }

    private void onDelete() {
        IndicatorValue selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) { showError("Выберите строку для удаления."); return; }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Удалить запись \"" + selected.getIndicatorName() + " / " + selected.getQuarter() + "\"?",
                ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText(null);
        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                try {
                    controller.deleteValue(
                        selected.getEnterpriseId(),
                        selected.getPeriodId(),
                        selected.getIndicatorId(),
                        selected.getCurrencyId()
                    );
                    loadTableData();
                } catch (Exception e) {
                    showError("Ошибка удаления: " + e.getMessage());
                }
            }
        });
    }


    private VBox buildIndicatorsPane() {
        VBox pane = new VBox(10);
        pane.setPadding(new Insets(10));

        Label lbl = new Label("Справочник показателей (название и валюта)");
        lbl.setStyle("-fx-font-weight: bold;");

        TableView<Indicator> indTable = new TableView<>();
        VBox.setVgrow(indTable, Priority.ALWAYS);

        TableColumn<Indicator, String> colName = new TableColumn<>("Название показателя");
        colName.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getName()));
        colName.setPrefWidth(300);

        TableColumn<Indicator, String> colCur = new TableColumn<>("Валюта");
        colCur.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCurrencyType()));
        colCur.setPrefWidth(100);

        indTable.getColumns().addAll(colName, colCur);

        Button addBtn = new Button("Добавить");
        Button editBtn = new Button("Редактировать");
        Button deleteBtn = new Button("Удалить");
        Button refreshBtn = new Button("Обновить");

        Runnable loadInd = () -> {
            try {
                indTable.setItems(FXCollections.observableArrayList(controller.getAllIndicators()));
            } catch (Exception e) {
                showError("Ошибка загрузки показателей: " + e.getMessage());
            }
        };

        addBtn.setOnAction(e -> showIndicatorForm(null, loadInd));
        editBtn.setOnAction(e -> {
            Indicator sel = indTable.getSelectionModel().getSelectedItem();
            if (sel == null) { showError("Выберите показатель."); return; }
            showIndicatorForm(sel, loadInd);
        });
        deleteBtn.setOnAction(e -> {
            Indicator sel = indTable.getSelectionModel().getSelectedItem();
            if (sel == null) { showError("Выберите показатель."); return; }
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "Удалить показатель \"" + sel.getName() + "\"?", ButtonType.YES, ButtonType.NO);
            confirm.setHeaderText(null);
            confirm.showAndWait().ifPresent(btn -> {
                if (btn == ButtonType.YES) {
                    try { controller.deleteIndicator(sel.getId()); loadInd.run(); }
                    catch (Exception ex) { showError("Ошибка: " + ex.getMessage()); }
                }
            });
        });
        refreshBtn.setOnAction(e -> loadInd.run());

        HBox btns = new HBox(10, addBtn, editBtn, deleteBtn, refreshBtn);
        pane.getChildren().addAll(lbl, indTable, btns);
        loadInd.run();
        return pane;
    }

    private void showIndicatorForm(Indicator indicator, Runnable onSave) {
        try {
            List<Currency> currencies = controller.getAllCurrencies();
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle(indicator == null ? "Добавить показатель" : "Редактировать показатель");

            TextField nameField = new TextField(indicator != null ? indicator.getName() : "");
            nameField.setPromptText("Название показателя");
            ComboBox<Currency> curCombo = new ComboBox<>(FXCollections.observableArrayList(currencies));
            if (indicator != null) {
                currencies.stream().filter(c -> c.getId() == indicator.getCurrencyId())
                        .findFirst().ifPresent(curCombo::setValue);
            }

            GridPane grid = new GridPane();
            grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(15));
            grid.add(new Label("Название:"), 0, 0); grid.add(nameField, 1, 0);
            grid.add(new Label("Валюта:"),   0, 1); grid.add(curCombo,  1, 1);

            dialog.getDialogPane().setContent(grid);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            dialog.showAndWait().ifPresent(btn -> {
                if (btn == ButtonType.OK) {
                    String name = nameField.getText().trim();
                    Currency cur = curCombo.getValue();
                    if (name.isEmpty() || cur == null) { showError("Заполните все поля."); return; }
                    try {
                        if (indicator == null) controller.addIndicator(name, cur.getId());
                        else controller.updateIndicator(indicator.getId(), name, cur.getId());
                        onSave.run();
                    } catch (Exception ex) { showError("Ошибка: " + ex.getMessage()); }
                }
            });
        } catch (Exception e) {
            showError("Ошибка загрузки: " + e.getMessage());
        }
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
