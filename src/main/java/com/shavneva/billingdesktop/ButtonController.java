package com.shavneva.billingdesktop;

import com.shavneva.billingdesktop.entity.Role;
import com.shavneva.billingdesktop.entity.User;
import com.shavneva.billingdesktop.service.ApiService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

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
    private CheckBox smsCheckBox;
    @FXML
    private CheckBox emailCheckBox;
    Image icon = new Image(LoginApplication.class.getResourceAsStream("/com/shavneva/billingdesktop/images/icon.png"));

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
                newStage.getIcons().add(icon);
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
                Role role = ApiService.getRoleName();
                Platform.runLater(() ->  openAdminWindow(event));
            } else {
                Platform.runLater(() -> ErrorDialog.showError("Ошибка аутентификации"));
            }
        });
    }
    private void openWindowForRole(Role role, ActionEvent event) {
        /*if (role == Role.ADMIN) {
            openAdminWindow(event);
        } else {
            openUserWindow(event);
        }*/

    }

    private void openUserWindow(ActionEvent event) {
        try {
            // Скрыть текущее окно
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.hide();

            // Загрузить FXML-файл окна пользователя
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("userwindow-view.fxml"));

            // Создать сцену и установить ее в новое окно
            Scene scene = new Scene(fxmlLoader.load());
            Stage newStage = new Stage();
            newStage.setTitle("Биллинговая система");
            newStage.getIcons().add(icon);
            newStage.setScene(scene);

            // Показать новое окно пользователя
            newStage.show();
        } catch (IOException e) {
            // Показать сообщение об ошибке при загрузке FXML-файла
            ErrorDialog.showError("Произошла ошибка: " + e.getMessage());
        }
    }

    private void openAdminWindow(ActionEvent event) {
        try {
            // Скрыть текущее окно
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.hide();

            // Загрузить FXML-файл окна пользователя
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("admin-view.fxml"));

            // Создать сцену и установить ее в новое окно
            Scene scene = new Scene(fxmlLoader.load());
            Stage newStage = new Stage();
            newStage.setTitle("Биллинговая система");
            newStage.getIcons().add(icon);
            newStage.setScene(scene);

            // Показать новое окно пользователя
            newStage.show();
        } catch (IOException e) {
            // Показать сообщение об ошибке при загрузке FXML-файла
            ErrorDialog.showError("Произошла ошибка: " + e.getMessage());
        }
    }

    public void handleAboutMenuItem(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Об аккаунте");
        alert.setHeaderText(null);
        alert.setContentText("Информация о вашем аккаунте");
        alert.showAndWait();
    }

    public void handleAboutExit(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Выход");
        alert.setHeaderText(null);
        alert.setContentText("Вы уверены, что хотите выйти?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Platform.exit();
        }
    }

    public void openSettingsMenuItem(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Настройки");
        alert.setHeaderText(null);

        smsCheckBox = new CheckBox("SMS уведомления");
        emailCheckBox = new CheckBox("Email уведомления");
        smsCheckBox.setSelected(false);
        emailCheckBox.setSelected(false);
        alert.getDialogPane().setContent(new VBox(smsCheckBox, emailCheckBox));

        alert.showAndWait();
    }

    public void openHelpMenuItem(ActionEvent event) {
        String helpContent = "Руководство пользователя для приложения управления биллинговой системой\n\n" +
                "1. Вкладка 'Профиль':\n" +
                "   - Во вкладке 'Профиль' вы можете просмотреть и отредактировать информацию о своем профиле\n" +
                "   - Информация о профиле может включать личные данные, такие как имя, фамилия, email и номер телефона\n" +
                "   - Для сохранения изменений нажмите кнопку 'Сохранить'\n" +
                "2. Настройки уведомлений:\n" +
                "   - Для настройки уведомлений выберите соответствующие опции в окне 'Настройки'\n" +
                "   - Опции могут включать выбор типа уведомлений (SMS, email и т. д.) и настройки частоты получения уведомлений\n" +
                "3. Просмотр тарифов:\n" +
                "   - Для просмотра доступных тарифов нажмите кнопку 'Тарифы'\n" +
                "   - В открывшемся окне отобразится список доступных тарифов\n" +
                "4. Выход из приложения:\n" +
                "   - Для выхода из приложения нажмите кнопку 'Выход'\n" +
                "   - Подтвердите желание выйти из приложения в появившемся диалоговом окне\n\n" +
                "Благодарим за использование нашего приложения!";
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Помощь");
        alert.setHeaderText(helpContent);
        alert.showAndWait();
    }

    public void openWindowAboutTariffs(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Тарифы");
        alert.setHeaderText(null);
        alert.setContentText("Список тарифов:");
        alert.showAndWait();
    }


}