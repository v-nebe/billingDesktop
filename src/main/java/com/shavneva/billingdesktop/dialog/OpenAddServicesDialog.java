package com.shavneva.billingdesktop.dialog;

import com.shavneva.billingdesktop.entity.Services;

import com.shavneva.billingdesktop.entity.Tariff;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class OpenAddServicesDialog extends Dialog<Services> {
    private TextField serviceNameField;

    public OpenAddServicesDialog() {
        setTitle("Добавить услугу");

        serviceNameField = new TextField();
        serviceNameField.setPromptText("Название услуги");

        GridPane grid = new GridPane();
        grid.add(new Label("Название:"), 0, 0);
        grid.add(serviceNameField, 1, 0);

        getDialogPane().setContent(grid);

        ButtonType saveButtonType = new ButtonType("Сохранить", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        setResultConverter(buttonType -> {
            if (buttonType == saveButtonType) {
                String name =  serviceNameField.getText().trim();
                if (name.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Ошибка", null, "Пожалуйста, введите название и цену тарифа.");
                    return null;
                }

                return new Services(null, name);
            }
            return null;
        });
    }
    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
