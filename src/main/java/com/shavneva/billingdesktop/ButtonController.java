package com.shavneva.billingdesktop;

import com.shavneva.billingdesktop.service.ApiService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class ButtonController {
    @FXML
    private TextField login;
    @FXML
    private TextField name;
    @FXML
    private TextField surname;
    @FXML
    private TextField phoneNumber;
    @FXML
    private TextField email;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField repeatedPassword;

    @FXML
    protected void openRegistrationWindowClick(ActionEvent event) {
        try {
                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentStage.hide();

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("registration-view.fxml"));

                Scene scene = new Scene(fxmlLoader.load(), 400, 290);
                Stage newStage = new Stage();
                newStage.hide();

                newStage.setTitle("Окно регистрации");
                newStage.setScene(scene);
                newStage.show();

            } catch (IOException e) {
                ErrorDialog.showError("Произошла ошибка при загрузке FXML: " + e.getMessage());
            }
    }
    @FXML
    protected void openUWindowClick(ActionEvent event){
        String firstName = name.getText();
        String lastName = surname.getText();
        String registrationEmail = email.getText();
        String pNumber =  phoneNumber.getText();
        String password = passwordField.getText();
        String rPassword = repeatedPassword.getText();

        // Отправить запрос регистрации через сервис
        ApiService.registerUser(firstName,lastName,registrationEmail,pNumber,password, isRegistered -> {
            if (isRegistered) {
                // В случае успешной аутентификации открыть окно пользователя
                Platform.runLater(() -> openUserWindow(event));
            } else {
                // Показать сообщение об ошибке регистрации
                Platform.runLater(() -> ErrorDialog.showError("Ошибка регистрации"));
            }
        });

    }

    @FXML
    protected void openUserWindowClick(ActionEvent event) {
        String userName = login.getText();
        String password = passwordField.getText();

        // Отправить запрос аутентификации через сервис
        ApiService.authenticateUser(userName, password, isAuthenticated -> {
            if (isAuthenticated) {
                // В случае успешной аутентификации открыть окно пользователя
                Platform.runLater(() -> openUserWindow(event));
            } else {
                // Показать сообщение об ошибке аутентификации
                Platform.runLater(() -> ErrorDialog.showError("Ошибка аутентификации"));
            }
        });
    }

    private void openUserWindow(ActionEvent event) {
        try {
            // Скрыть текущее окно
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.hide();

            // Загрузить FXML-файл окна пользователя
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/userwindow-view.fxml"));

            // Создать сцену и установить ее в новое окно
            Scene scene = new Scene(fxmlLoader.load(), 400, 290);
            Stage newStage = new Stage();
            newStage.setTitle("Биллинговая система");
            newStage.setScene(scene);

            // Показать новое окно пользователя
            newStage.show();
        } catch (IOException e) {
            // Показать сообщение об ошибке при загрузке FXML-файла
            ErrorDialog.showError("Произошла ошибка: " + e.getMessage());
        }
    }
}