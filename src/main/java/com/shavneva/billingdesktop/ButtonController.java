package com.shavneva.billingdesktop;

import com.shavneva.billingdesktop.dialog.ErrorDialog;
import com.shavneva.billingdesktop.entity.*;
import com.shavneva.billingdesktop.service.ApiService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

public class ButtonController {

    private User currentUser;
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
    @FXML
    private TextField amountField;
    Image icon = new Image(LoginApplication.class.getResourceAsStream("/com/shavneva/billingdesktop/images/icon.png"));

    @FXML
    protected void openRegistrationWindowClick(ActionEvent event) {
        try {
                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentStage.hide();

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("registration-view.fxml"));

                Scene scene = new Scene(fxmlLoader.load());
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

        ApiService.authenticateUser(userName, password, isAuthenticated -> {
            if (isAuthenticated) {
                ApiService.getUserInfo(userName, user -> {
                    InfoContext.setCurrentUser(user);

                    ApiService.getCurrentUserRole(role -> {
                        InfoContext.setRole(role);
                        Platform.runLater(() -> openWindowForRole(role, event));
                    });
                });
            } else {
                Platform.runLater(() -> ErrorDialog.showError("Ошибка аутентификации"));
            }
        });
    }
    private void openWindowForRole(Role role, ActionEvent event) {
        if (role == Role.ADMIN) {
            openAdminWindow(event);
        } else {
            openUserWindow(event);
        }
    }

    private void openUserWindow(ActionEvent event) {
        try {
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
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.hide();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("admin-view.fxml"));

            Scene scene = new Scene(fxmlLoader.load());
            Stage newStage = new Stage();
            newStage.setTitle("Биллинговая система");
            newStage.getIcons().add(icon);
            newStage.setScene(scene);

            newStage.show();
        } catch (IOException e) {

            ErrorDialog.showError("Произошла ошибка: " + e.getMessage());
        }
    }

    @FXML
    private void openDepositMoneyWindow(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("depositMoney-view.fxml"));

            Scene scene = new Scene(fxmlLoader.load());
            Stage newStage = new Stage();
            newStage.setTitle("Пополнить счёт");
            newStage.getIcons().add(icon);
            newStage.setScene(scene);
            newStage.show();

        } catch (IOException e) {
            ErrorDialog.showError("Произошла ошибка: " + e.getMessage());
        }
    }

    @FXML
    protected void handleDepositButtonClick(ActionEvent event) {

        String amountText = amountField.getText();
        BigDecimal amount = new BigDecimal(amountText);
        User currentUser = InfoContext.getCurrentUser();

        ApiService.depositMoney(currentUser.getEmail(), amount);

        amountField.getScene().getWindow().hide();
    }

    public void handleAboutMenuItem(ActionEvent event) {
        User currentUser = InfoContext.getCurrentUser();
        if (currentUser != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Об аккаунте");
            alert.setHeaderText(null);
            alert.setContentText("Информация о вашем аккаунте\n\n" +
                    "Имя: " + currentUser.getFirstName() + "\n" +
                    "Фамилия: " + currentUser.getLastName() + "\n" +
                    "Почта: " + currentUser.getEmail() + "\n" +
                    "Номер телефона: " + currentUser.getNumber()
            );
            alert.showAndWait();
        } else {
            // Отобразить сообщение об ошибке, если currentUser или role равны null
            ErrorDialog.showError("Информация о пользователе недоступна");
        }
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
        alert.setTitle("Настройки уведомлений");
        alert.setHeaderText(null);

        RadioButton smsRadioButton = new RadioButton("SMS уведомления");
        RadioButton emailRadioButton = new RadioButton("Email уведомления");
        emailRadioButton.setSelected(true);

        ToggleGroup toggleGroup = new ToggleGroup();
        smsRadioButton.setToggleGroup(toggleGroup);
        emailRadioButton.setToggleGroup(toggleGroup);

        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(smsRadioButton, emailRadioButton);
        vbox.setPadding(new Insets(20));

        alert.getDialogPane().setContent(vbox);

        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(okButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == okButton) {
            boolean smsNotifications = smsRadioButton.isSelected();
            boolean emailNotifications = emailRadioButton.isSelected();

            ApiService.saveNotificationSettings(smsNotifications, emailNotifications);
        }
    }

    public void openHelpMenuItem(ActionEvent event) {
        String helpContent = "Руководство пользователя для приложения управления биллинговой системой\n\n" +
                "1. Вкладка 'Профиль':\n" +
                "   - Во вкладке 'Профиль' вы можете просмотреть информацию о своем профиле\n" +
                "   - Информация о профиле включает личные данные, такие как имя, фамилия, email и номер телефона\n" +
                "2. Настройки уведомлений:\n" +
                "   - Для настройки уведомлений выберите соответствующие опции в окне 'Настройки'\n" +
                "   - Опции включают выбор типа уведомлений: SM или email\n" +
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
        ApiService.getTariffWithServices(tariffs -> {
            Platform.runLater(() -> {
                if (tariffs != null && !tariffs.isEmpty()) {
                    StringBuilder tariffsInfo = new StringBuilder();
                    for (Tariff tariff : tariffs) {
                        tariffsInfo.append("Название тарифа: ").append(tariff.getTariffName()).append("\n")
                                .append("Цена: ").append(tariff.getPrice()).append("\n")
                                .append("Входящие услуги:\n");

                        for (Services service : tariff.getServices()) {
                            tariffsInfo.append("- ").append(service.getService()).append("\n");
                        }
                        tariffsInfo.append("\n");
                    }

                    // Отобразить информацию о тарифах в диалоговом окне
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("О тарифах");
                    alert.setHeaderText(null);
                    alert.setContentText(tariffsInfo.toString());
                    alert.showAndWait();
                } else {
                    ErrorDialog.showError("Информация о тарифах недоступна");
                }
            });
        });
    }


}