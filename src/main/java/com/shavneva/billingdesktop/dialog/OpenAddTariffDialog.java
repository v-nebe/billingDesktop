package com.shavneva.billingdesktop.dialog;

import com.shavneva.billingdesktop.entity.Tariff;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.math.BigDecimal;

public class OpenAddTariffDialog extends Dialog<Tariff> {
    public OpenAddTariffDialog() {
        setTitle("Добавить тариф");

        TextField tariffNameField = new TextField();
        tariffNameField.setPromptText("Название тарифа");
        TextField priceField = new TextField();
        priceField.setPromptText("Цена");

        GridPane grid = new GridPane();
        grid.add(new Label("Название:"), 0, 0);
        grid.add(tariffNameField, 1, 0);
        grid.add(new Label("Цена:"), 0, 1);
        grid.add(priceField, 1, 1);

        getDialogPane().setContent(grid);

        ButtonType saveButtonType = new ButtonType("Сохранить", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        setResultConverter(buttonType -> {
            if (buttonType == saveButtonType) {
                String name = tariffNameField.getText().trim();
                String priceStr = priceField.getText().trim();
                if (name.isEmpty() || priceStr.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Ошибка", null, "Пожалуйста, введите название и цену тарифа.");
                    return null;
                }

                BigDecimal price;
                try {
                    price = new BigDecimal(priceStr);
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Ошибка", null, "Пожалуйста, введите корректную цену.");
                    return null;
                }

                return new Tariff(null, name, price, null);
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
