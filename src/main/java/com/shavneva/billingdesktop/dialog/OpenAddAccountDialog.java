package com.shavneva.billingdesktop.dialog;

import com.shavneva.billingdesktop.entity.Account;

import com.shavneva.billingdesktop.entity.Services;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.math.BigDecimal;

public class OpenAddAccountDialog extends Dialog<Account> {
    private TextField amountField;

    public OpenAddAccountDialog() {
        setTitle("Добавить аккаунт");

        amountField = new TextField();
        amountField.setPromptText("Сумма");

        GridPane grid = new GridPane();
        grid.add(new Label("Сумма:"), 0, 0);
        grid.add(amountField, 1, 0);

        getDialogPane().setContent(grid);

        ButtonType saveButtonType = new ButtonType("Сохранить", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);


        setResultConverter(buttonType -> {
            if (buttonType == saveButtonType) {
                String amountStr = amountField.getText().trim();
                if (amountStr.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Ошибка", null, "Пожалуйста, введите сумму.");
                    return null;
                }

                try {
                    BigDecimal amount = new BigDecimal(amountStr);
                    return new Account(null, amount);
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Ошибка", null, "Пожалуйста, введите корректную сумму.");
                    return null;
                }
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
