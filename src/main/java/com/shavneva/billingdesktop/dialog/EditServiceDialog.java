package com.shavneva.billingdesktop.dialog;

import com.shavneva.billingdesktop.entity.Services;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

import java.math.BigDecimal;


public class EditServiceDialog extends Dialog<Boolean> {
    private TextField serviceNameField;

    public EditServiceDialog(Services service) {
        setTitle("Редактировать услугу");

        serviceNameField = new TextField(service.getService());

        GridPane grid = new GridPane();
        grid.add(new Label("Название:"), 0, 0);
        grid.add(serviceNameField, 1, 0);

        getDialogPane().setContent(grid);

        ButtonType saveButtonType = new ButtonType("Сохранить", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        setResultConverter(buttonType -> {
            if (buttonType == saveButtonType) {
                String name = serviceNameField.getText().trim();
                if (name.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Ошибка", null, "Пожалуйста, введите название услуги.");
                    return false;
                }

                service.setService(name);
                return true;
            }
            return false;
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