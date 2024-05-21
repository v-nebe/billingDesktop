package com.shavneva.billingdesktop;

import com.shavneva.billingdesktop.dialog.ErrorDialog;
import com.shavneva.billingdesktop.entity.*;
import com.shavneva.billingdesktop.service.ApiService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
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
    private TextField amountField;
    @FXML
    private Label userAmount;
    @FXML
    Image icon = new Image(LoginApplication.class.getResourceAsStream("/com/shavneva/billingdesktop/images/icon.png"));

    @FXML
    private void initialize() {
        userAmount = new Label("Загрузка...");
        userAmount.setFont(Font.font(20));
    }

    @FXML
    protected void openRegistrationWindowClick(ActionEvent event) {
        try {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.hide();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("registration-view.fxml"));
            Parent root = fxmlLoader.load();
            Stage newStage = new Stage();
            newStage.setTitle("Окно регистрации");
            newStage.getIcons().add(icon);
            newStage.setScene(new Scene(root));
            newStage.setResizable(false);

            newStage.setOnCloseRequest(windowEvent -> {
                openLoginWindow(event);
            });

            newStage.show();

        } catch (IOException e) {
            ErrorDialog.showError("Произошла ошибка при загрузке FXML: " + e.getMessage());
        }
    }

    private void openLoginWindow(ActionEvent event) {
        try {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.hide();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login-view.fxml"));
            Parent root = fxmlLoader.load();
            Stage loginStage = new Stage();
            loginStage.setTitle("Окно логинации");
            loginStage.getIcons().add(icon);
            loginStage.setScene(new Scene(root));
            loginStage.show();
        } catch (IOException e) {
            ErrorDialog.showError("Произошла ошибка при загрузке FXML: " + e.getMessage());
        }
    }

    @FXML
    protected void openUWindowClick(ActionEvent event) {
        try {
            String firstName = name.getText();
            String lastName = surname.getText();
            String registrationEmail = email.getText();
            String pNumber = phoneNumber.getText();
            String password = passwordField.getText();
            String rPassword = repeatedPassword.getText();

            if (!password.equals(rPassword)) {
                ErrorDialog.showError("Пароли не совпадают. Перепроверьте ввод");
                return;
            }

            ApiService.registerUser(firstName, lastName, registrationEmail, pNumber, password, isRegistered -> {
                if (isRegistered) {
                    Platform.runLater(() -> openLoginWindow(event));
                } else {
                    Platform.runLater(() -> ErrorDialog.showError("Ошибка регистрации"));
                }
            });
        } catch (Exception e) {
            ErrorDialog.showError("Введите все данные для регистрации");
        }
    }

    @FXML
    protected void openUserWindowClick(ActionEvent event) {
        try {
            String userName = login.getText();
            String password = passwordField.getText();

            ApiService.authenticateUser(userName, password, isAuthenticated -> {
                if (isAuthenticated) {
                    ApiService.getAllUserInfo(users -> {
                        User currentUser = findUserByUsername(users, userName);
                        if (currentUser != null) {
                            InfoContext.setCurrentUser(currentUser);

                            List<Role> roles = currentUser.getRoles();
                            if (roles != null && !roles.isEmpty()) {
                                Role userRole = roles.get(0);
                                InfoContext.setRole(userRole);
                                Platform.runLater(() -> openWindowForRole(userRole, event));
                            } else {
                                Platform.runLater(() -> ErrorDialog.showError("Роль пользователя не найдена"));
                            }
                        } else {
                            Platform.runLater(() -> ErrorDialog.showError("Ошибка при получении данных пользователя"));
                        }
                    });
                } else {
                    Platform.runLater(() -> ErrorDialog.showError("Ошибка аутентификации"));
                }
            });
        } catch (Exception e) {
            ErrorDialog.showError("Введите корректные данные для входа");
        }
    }

    private User findUserByUsername(List<User> users, String username) {
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(username)) {
                return user;
            }
        }
        return null;
    }

    private void openWindowForRole(Role role, ActionEvent event) {
        if ("ROLE_ADMIN".equals(role.getRoleName())) {
            openAdminWindow(event);
        } else {
            openUserWindow(event);
        }
    }

    private void openUserWindow(ActionEvent event) {
        try {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.hide();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("userwindow-view.fxml"));
            Parent root = fxmlLoader.load();

            GridPane gridPane = (GridPane) root.lookup("#gridPane");

            userAmount.setFont(Font.font(20));
            GridPane.setHalignment(userAmount, HPos.CENTER);
            GridPane.setConstraints(userAmount, 1, 0);
            gridPane.getChildren().add(userAmount);

            Label userTariff = new Label("Загрузка...");
            userTariff.setFont(Font.font(20));
            GridPane.setHalignment(userTariff, HPos.CENTER);
            GridPane.setConstraints(userTariff, 1, 1);
            gridPane.getChildren().add(userTariff);

            Scene scene = new Scene(root);
            Stage newStage = new Stage();
            newStage.setTitle("Биллинговая система");
            newStage.getIcons().add(icon);
            newStage.setResizable(false);
            newStage.setScene(scene);

            newStage.setOnCloseRequest(windowEvent -> {
                openLoginWindow(event);
            });

            ApiService.getAllUserInfo(users -> {
                User currentUser = InfoContext.getCurrentUser();
                if (currentUser != null) {
                    Optional<User> optionalUser = users.stream()
                            .filter(user -> user.getUserId().equals(currentUser.getUserId()))
                            .findFirst();

                    if (optionalUser.isPresent()) {
                        User foundUser = optionalUser.get();
                        InfoContext.setCurrentUser(foundUser);

                        Tariff tariff = foundUser.getTariff();
                        Account account = foundUser.getAccount();

                        Platform.runLater(() -> {
                            userAmount.setText(String.valueOf(account.getAmount()));
                            userTariff.setText(tariff.getTariffName());
                            newStage.show();
                        });
                    } else {
                        ErrorDialog.showError("Не удалось найти информацию о текущем пользователе");
                    }
                } else {
                    ErrorDialog.showError("Текущий пользователь не установлен");
                }
            });
            newStage.show();
        } catch (IOException e) {
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
            newStage.setResizable(false);
            newStage.setScene(scene);

            newStage.setOnCloseRequest(windowEvent -> {
                openLoginWindow(event);
            });

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
            newStage.setResizable(false);
            newStage.show();

            newStage.setOnCloseRequest(windowEvent -> {
                openUserWindow(event);
            });

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.hide();

        } catch (IOException e) {
            ErrorDialog.showError("Произошла ошибка: " + e.getMessage());
        }
    }

    @FXML
    protected void handleDepositButtonClick(ActionEvent event) {
        try {
        String amountText = amountField.getText();
        BigDecimal amount = new BigDecimal(amountText);
        User currentUser = InfoContext.getCurrentUser();

        ApiService.depositMoney(currentUser.getEmail(), amount);
        openUserWindow(event);

        amountField.getScene().getWindow().hide();
        } catch (Exception e) {
            ErrorDialog.showError("Произошла ошибка при пополнении счета: " + e.getMessage());
        }
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
        User currentUser = InfoContext.getCurrentUser();
        ApiService.getNotificationType(currentUser.getEmail(), (currentNotificationType) -> {
            Platform.runLater(() -> {
                try {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Настройки уведомлений");
                    alert.setHeaderText(null);

                    RadioButton smsRadioButton = new RadioButton("SMS уведомления");
                    RadioButton emailRadioButton = new RadioButton("Email уведомления");

                    // Устанавливаем выбор уведомлений в соответствии с текущим типом
                    if ("sms".equalsIgnoreCase(currentNotificationType)) {
                        smsRadioButton.setSelected(true);
                    } else {
                        emailRadioButton.setSelected(true);
                    }

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
                        // Сохраняем новые настройки уведомлений в базе данных
                        String newNotificationType = smsRadioButton.isSelected() ? "sms" : "email";
                        ApiService.saveNotificationType(currentUser.getEmail(), newNotificationType, () -> {
                            // Новые настройки успешно сохранены
                            Platform.runLater(() -> {
                                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                                successAlert.setTitle("Успех");
                                successAlert.setHeaderText(null);
                                successAlert.setContentText("Настройки уведомлений успешно сохранены");
                                successAlert.showAndWait();
                            });
                        });
                    }
                } catch (Exception e) {
                    // Обработка ошибок при получении или сохранении настроек уведомлений
                    ErrorDialog.showError("Произошла ошибка при обработке настроек уведомлений: " + e.getMessage());
                }
            });
        });
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