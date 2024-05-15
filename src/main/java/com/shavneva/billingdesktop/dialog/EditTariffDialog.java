package com.shavneva.billingdesktop.dialog;

import com.shavneva.billingdesktop.entity.Tariff;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.math.BigDecimal;

public class EditTariffDialog extends Dialog<Boolean> {
    private TextField tariffIdField;
    private TextField tariffNameField;
    private TextField priceField;

    public EditTariffDialog(Tariff tariff) {
        setTitle("Редактировать тариф");

        tariffIdField = new TextField(String.valueOf(tariff.getTariffId()));
        tariffIdField.setEditable(false);

        tariffNameField = new TextField(tariff.getTariffName());
        tariffNameField.setPromptText("Название тарифа");

        priceField = new TextField(tariff.getPrice().toString());
        priceField.setPromptText("Цена");

        GridPane grid = new GridPane();
        grid.add(new Label("ID:"), 0, 0);
        grid.add(tariffIdField, 1, 0);
        grid.add(new Label("Название:"), 0, 1);
        grid.add(tariffNameField, 1, 1);
        grid.add(new Label("Цена:"), 0, 2);
        grid.add(priceField, 1, 2);

        getDialogPane().setContent(grid);

        ButtonType saveButtonType = new ButtonType("Сохранить", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        setResultConverter(buttonType -> {
            if (buttonType == saveButtonType) {
                String name = tariffNameField.getText().trim();
                String priceStr = priceField.getText().trim();
                if (name.isEmpty() || priceStr.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Ошибка", null, "Пожалуйста, введите название и цену тарифа.");
                    return false;
                }

                BigDecimal price;
                try {
                    price = new BigDecimal(priceStr);
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Ошибка", null, "Пожалуйста, введите корректную цену.");
                    return false;
                }

                // Обновляем информацию о тарифе
                tariff.setTariffName(name);
                tariff.setPrice(price);
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