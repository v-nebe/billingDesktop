package com.shavneva.billingdesktop.dialog;

import com.shavneva.billingdesktop.entity.User;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

public class EditUserDialog extends Dialog<Boolean> {
    private TextField userIdField; // Добавляем поле для отображения id пользователя
    private TextField firstNameField;
    private TextField lastNameField;
    private TextField emailField;
    private TextField numberField;
    private PasswordField passwordField;

    public EditUserDialog(User user) {

        setTitle("Редактировать пользователя");

        userIdField = new TextField(String.valueOf(user.getUserId()));
        userIdField.setEditable(false);

        firstNameField = new TextField(user.getFirstName());
        lastNameField = new TextField(user.getLastName());
        emailField = new TextField(user.getEmail());
        numberField = new TextField(user.getNumber());
        passwordField = new PasswordField();
        passwordField.setPromptText("Введите новый пароль");

        // Создание сетки для расположения элементов управления
        GridPane grid = new GridPane();
        grid.add(new Label("ID:"), 0, 0);
        grid.add(userIdField, 1, 0);
        grid.add(new Label("Имя:"), 0, 1);
        grid.add(firstNameField, 1, 1);
        grid.add(new Label("Фамилия:"), 0, 2);
        grid.add(lastNameField, 1, 2);
        grid.add(new Label("Email:"), 0, 3);
        grid.add(emailField, 1, 3);
        grid.add(new Label("Номер телефона:"), 0, 4);
        grid.add(numberField, 1, 4);
        grid.add(new Label("Новый пароль:"), 0, 5);
        grid.add(passwordField, 1, 5);

        getDialogPane().setContent(grid);

        ButtonType saveButtonType = new ButtonType("Сохранить", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        setResultConverter(buttonType -> {
            if (buttonType == saveButtonType) {

                user.setFirstName(firstNameField.getText());
                user.setLastName(lastNameField.getText());
                user.setEmail(emailField.getText());
                user.setNumber(numberField.getText());
                if (!passwordField.getText().isEmpty()) {
                    user.setPassword(passwordField.getText());
                }
                return true;
            }
            return false;
        });
    }
}