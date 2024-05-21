package com.shavneva.billingdesktop;

import com.shavneva.billingdesktop.dialog.*;
import com.shavneva.billingdesktop.entity.*;
import com.shavneva.billingdesktop.repository.CrudRepository;
import com.shavneva.billingdesktop.repository.factory.CrudFactory;
import com.shavneva.billingdesktop.service.ApiService;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.xwpf.usermodel.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DataBaseLoader {
    public void showTableUser(ActionEvent event) {
        TableView<User> table = new TableView<>();

        TableColumn<User, String> firstNameColumn = new TableColumn<>("Имя");
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<User, String> lastNameColumn = new TableColumn<>("Фамилия");
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<User, String> emailColumn = new TableColumn<>("Почта");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<User, String> numberColumn = new TableColumn<>("Номер телефона");
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));

        TableColumn<User, String> passwordColumn = new TableColumn<>("Пароль");
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));

        ApiService.getAllUserInfo(users -> {
            Platform.runLater(() -> {
                ObservableList<User> userObservableList = FXCollections.observableArrayList(users);
                table.setItems(userObservableList);
            });
        });

        TableColumn<User, String> tariffColumn = new TableColumn<>("Тариф");
        tariffColumn.setCellValueFactory(data -> {
            User user = data.getValue();
            if (user.getTariff() != null) {
                return new SimpleStringProperty(user.getTariff().getTariffName());
            } else {
                return new SimpleStringProperty("Не указан");
            }
        });

        TableColumn<User, String> accountColumn = new TableColumn<>("Счет");
        accountColumn.setCellValueFactory(data -> {
            User user = data.getValue();
            if (user.getAccount() != null) {
                return new SimpleStringProperty(String.valueOf(user.getAccount().getAmount()));
            } else {
                return new SimpleStringProperty("Не указан");
            }
        });

        TableColumn<User, String> roleColumn = new TableColumn<>("Роль");
        roleColumn.setCellValueFactory(data -> {
            User user = data.getValue();
            if (user.getRoles() != null && !user.getRoles().isEmpty()) {
                String roles = user.getRoles().stream()
                        .map(Role::getRoleName)
                        .collect(Collectors.joining(", "));
                return new SimpleStringProperty(roles);
            } else {
                return new SimpleStringProperty("Не указан");
            }
        });

        // Добавление столбцов в таблицу
        table.getColumns().addAll(firstNameColumn, lastNameColumn, emailColumn, numberColumn, passwordColumn,
                tariffColumn, accountColumn, roleColumn);
        Button deleteButton = new Button("Удалить");

        deleteButton.setOnAction(e -> deleteUser(table));
        Button editButton = new Button("Редактировать");
        editButton.setOnAction(e -> editUser(table));


        HBox buttonBox = new HBox(10);
        buttonBox.setPadding(new Insets(10));
        buttonBox.getChildren().addAll(deleteButton, editButton);

        BorderPane root = new BorderPane();
        root.setTop(buttonBox);
        root.setCenter(table);

        Scene scene = new Scene(root, 800, 600);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Таблица Пользователи");
        stage.show();
    }

    private void deleteUser(TableView<User> table) {
        User user = table.getSelectionModel().getSelectedItem();
        if (user != null) {
            CrudRepository<User> userCrudRepository = CrudFactory.createUserRepository();
            userCrudRepository.delete(String.valueOf(user.getUserId()));
            table.getItems().remove(user);
        } else {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Предупреждение");
            alert.setHeaderText(null);
            alert.setContentText("Пожалуйста, выберите пользователя для удаления.");
            alert.showAndWait();
         }
    }

    private void editUser(TableView<User> table) {
        User user = table.getSelectionModel().getSelectedItem();
        if (user != null) {
            EditUserDialog dialog = new EditUserDialog(user);

            Stage stage = (Stage) table.getScene().getWindow();
            dialog.initOwner(stage);
            dialog.initModality(Modality.WINDOW_MODAL);

            Optional<Boolean> result = dialog.showAndWait();

            if (result.isPresent() && result.get()) {
                CrudRepository<User> userCrudRepository = CrudFactory.createUserRepository();
                userCrudRepository.update(user);
                table.refresh();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Предупреждение");
            alert.setHeaderText(null);
            alert.setContentText("Пожалуйста, выберите пользователя для редактирования.");
            alert.showAndWait();
        }
    }

    public void showTableRoles(ActionEvent event) {
        TableView<Role> table = new TableView<>();

        TableColumn<Role, String> roleColumn = new TableColumn<>("Роль");
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("roleName"));

        table.getColumns().addAll(roleColumn);

        CrudRepository<Role> roleCrudRepository = CrudFactory.createRoleRepository();
        List<Role> roleList = roleCrudRepository.getAll();

        ObservableList<Role> servicesObservableList = FXCollections.observableArrayList(roleList);
        table.setItems(servicesObservableList);

        BorderPane root = new BorderPane();
        root.setCenter(table);
        Scene scene = new Scene(root, 800, 600);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Таблица");
        stage.show();
    }
    public void showTableTariffs(ActionEvent event) {
        TableView<Tariff> table = new TableView<>();

        TableColumn<Tariff, String> tariffNameColumn = new TableColumn<>("Название тарифа");
        tariffNameColumn.setCellValueFactory(new PropertyValueFactory<>("tariffName"));

        TableColumn<Tariff, String> priceColumn = new TableColumn<>("Цена");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));


        table.getColumns().addAll( tariffNameColumn, priceColumn);


        CrudRepository<Tariff> tariffCrudRepository = CrudFactory.createTariffRepository();
        List<Tariff> tarriffList = tariffCrudRepository.getAll();

        ObservableList<Tariff> tariffObservableList = FXCollections.observableArrayList(tarriffList);
        table.setItems(tariffObservableList);

        Button deleteButton = new Button("Удалить");
        deleteButton.setOnAction(e -> deleteTariff(table));

        Button editButton = new Button("Редактировать");
        editButton.setOnAction(e -> editTariff(table));

        Button addButton = new Button("Добавить");
        addButton.setOnAction(e -> addTariff(table));

        HBox buttonBox = new HBox(10);
        buttonBox.setPadding(new Insets(10));
        buttonBox.getChildren().addAll(deleteButton, editButton, addButton);

        BorderPane root = new BorderPane();
        root.setTop(buttonBox);
        root.setCenter(table);

        Scene scene = new Scene(root, 800, 600);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Таблица Тарифы");
        stage.show();
    }

    private void deleteTariff(TableView<Tariff> table) {
        Tariff tariff = table.getSelectionModel().getSelectedItem();
        if (tariff  != null) {
            CrudRepository<Tariff> tariffCrudRepository = CrudFactory.createTariffRepository();
            tariffCrudRepository.delete(String.valueOf(tariff.getTariffId()));
            table.getItems().remove(tariff );
        } else {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Предупреждение");
            alert.setHeaderText(null);
            alert.setContentText("Пожалуйста, выберите тариф для удаления.");
            alert.showAndWait();
        }
    }

    private void editTariff(TableView<Tariff> table) {
        Tariff tariff = table.getSelectionModel().getSelectedItem();
        if (tariff != null) {
            EditTariffDialog dialog = new EditTariffDialog(tariff);

            Stage stage = (Stage) table.getScene().getWindow();
            dialog.initOwner(stage);
            dialog.initModality(Modality.WINDOW_MODAL);

            Optional<Boolean> result = dialog.showAndWait();

            if (result.isPresent() && result.get()) {
                CrudRepository<Tariff> tariffCrudRepository = CrudFactory.createTariffRepository();
                tariffCrudRepository.update(tariff);
                table.refresh();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Предупреждение");
            alert.setHeaderText(null);
            alert.setContentText("Пожалуйста, выберите тариф для редактирования.");
            alert.showAndWait();
        }
    }

    private void addTariff(TableView<Tariff> table) {
        OpenAddTariffDialog dialog = new OpenAddTariffDialog();

        Optional<Tariff> result = dialog.showAndWait();

        if (result.isPresent()) {
            Tariff newTariff = result.get();
            CrudRepository<Tariff> tariffRepository = CrudFactory.createTariffRepository();
            tariffRepository.create(newTariff);

            table.getItems().add(newTariff);
        }
    }

    public void showTableServices(ActionEvent event) {
        TableView<Services> table = new TableView<>();

        TableColumn<Services, String> priceColumn = new TableColumn<>("Услуги");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("service"));

        table.getColumns().addAll( priceColumn);

        CrudRepository<Services> servicesCrudRepository = CrudFactory.createServicesRepository();
        List<Services> servicesList = servicesCrudRepository.getAll();

        ObservableList<Services> servicesObservableList = FXCollections.observableArrayList(servicesList);
        table.setItems(servicesObservableList);

        Button deleteButton = new Button("Удалить");
        deleteButton.setOnAction(e -> deleteService(table));

        Button editButton = new Button("Редактировать");
        editButton.setOnAction(e -> editService(table));

        Button addButton = new Button("Добавить");
        addButton.setOnAction(e -> addService(table));

        HBox buttonBox = new HBox(10);
        buttonBox.setPadding(new Insets(10));
        buttonBox.getChildren().addAll(deleteButton, editButton, addButton);
        BorderPane root = new BorderPane();
        root.setTop(buttonBox);
        root.setCenter(table);

        Scene scene = new Scene(root, 800, 600);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Таблица Услуги");
        stage.show();
    }

    private void deleteService(TableView<Services> table) {
        Services service = table.getSelectionModel().getSelectedItem();
        if (service  != null) {
            CrudRepository<Services> serviceCrudRepository = CrudFactory.createServicesRepository();
            serviceCrudRepository.delete(String.valueOf(service.getServiceId()));
            table.getItems().remove(service);
        } else {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Предупреждение");
            alert.setHeaderText(null);
            alert.setContentText("Пожалуйста, выберите услуги для удаления.");
            alert.showAndWait();
        }
    }

    private void editService(TableView<Services> table) {
        Services selectedService = table.getSelectionModel().getSelectedItem();
        if (selectedService != null) {

            EditServiceDialog dialog = new EditServiceDialog(selectedService);

            Stage stage = (Stage) table.getScene().getWindow();
            dialog.initOwner(stage);
            dialog.initModality(Modality.WINDOW_MODAL);

            Optional<Boolean> result = dialog.showAndWait();

            if (result.isPresent() && result.get()) {
                CrudRepository<Services> servicesCrudRepository = CrudFactory.createServicesRepository();
                servicesCrudRepository.update(selectedService);
                table.refresh();
            }

            table.refresh();
        } else {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Предупреждение");
            alert.setHeaderText(null);
            alert.setContentText("Пожалуйста, выберите услугу для редактирования.");
            alert.showAndWait();
        }

    }

    private void addService(TableView<Services> table) {
        OpenAddServicesDialog dialog = new OpenAddServicesDialog();

        Optional<Services> result = dialog.showAndWait();

        if (result.isPresent()) {
            Services newService = result.get();
            CrudRepository<Services> servicesRepository = CrudFactory.createServicesRepository();
            servicesRepository.create(newService);

            table.getItems().add(newService);
        }
    }


    public void showTableAccount(ActionEvent event) {
        TableView<Account> table = new TableView<>();

        TableColumn<Account, String> amount = new TableColumn<>("Счёт");
        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));

        table.getColumns().addAll(amount);

        CrudRepository<Account> accountCrudRepository = CrudFactory.createAccountRepository();
        List<Account> accountList = accountCrudRepository.getAll();

        ObservableList<Account> accountObservableList = FXCollections.observableArrayList(accountList);
        table.setItems(accountObservableList);

        Button deleteButton = new Button("Удалить");
        deleteButton.setOnAction(e -> deleteAccount(table));

        Button addButton = new Button("Добавить");
        addButton.setOnAction(e -> addAccount(table));

        HBox buttonBox = new HBox(10);
        buttonBox.setPadding(new Insets(10));
        buttonBox.getChildren().addAll(deleteButton, addButton);
        BorderPane root = new BorderPane();
        root.setTop(buttonBox);
        root.setCenter(table);

        Scene scene = new Scene(root, 800, 600);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Таблица Счета");
        stage.show();
    }

    private void deleteAccount(TableView<Account> table) {
        Account account = table.getSelectionModel().getSelectedItem();
        if ( account  != null) {
            CrudRepository<Account> accountCrudRepository = CrudFactory.createAccountRepository();
            accountCrudRepository.delete(String.valueOf( account.getAccountId()));
            table.getItems().remove( account);
        } else {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Предупреждение");
            alert.setHeaderText(null);
            alert.setContentText("Пожалуйста, выберите услуги для удаления.");
            alert.showAndWait();
        }
    }

    private void addAccount(TableView<Account> table) {
        OpenAddAccountDialog dialog = new OpenAddAccountDialog();

        Optional<Account> result = dialog.showAndWait();

        if (result.isPresent()) {
            Account newAccount = result.get();
            CrudRepository<Account> accountRepository = CrudFactory.createAccountRepository();
            accountRepository.create(newAccount);

            table.getItems().add(newAccount);
        }
    }

    public void generateUserReport(ActionEvent event) {
        // Вызов метода из ApiService для получения информации о пользователях
        ApiService.getAllUserInfo(users -> {
            // Создание нового документа
            XWPFDocument document = new XWPFDocument();

            // Добавление заголовка
            XWPFParagraph title = document.createParagraph();
            title.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun titleRun = title.createRun();
            titleRun.setText("Отчет о всех пользователях");
            titleRun.setBold(true);
            titleRun.setFontSize(20);

            // Добавление информации о пользователях
            for (User user : users) {
                addUserInfo(document, "Имя:", user.getFirstName());
                addUserInfo(document, "Фамилия:", user.getLastName());
                addUserInfo(document, "Номер телефона:", user.getNumber());

                Tariff tariff = user.getTariff();
                if (tariff != null) {
                    addUserInfo(document, "Тарифный план:", tariff.getTariffName());
                } else {
                    addUserInfo(document, "Тарифный план:", "Не указан");
                }

                Account account = user.getAccount();
                if (account != null) {
                    addUserInfo(document, "Баланс:", String.valueOf(account.getAmount()));
                } else {
                    addUserInfo(document, "Баланс:", "Не указан");
                }

                // Добавление пустой строки для разделения пользователей
                document.createParagraph();
            }

            // Сохранение файла в отдельном потоке
            Platform.runLater(() -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Сохранить отчет");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Word Documents", "*.docx"));
                File file = fileChooser.showSaveDialog(new Stage());

                if (file != null) {
                    try (FileOutputStream out = new FileOutputStream(file)) {
                        document.write(out);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Сохранение");
                        alert.setHeaderText(null);
                        alert.setContentText("Отчет успешно сохранен.");
                        alert.showAndWait();
                    } catch (IOException e) {
                       ErrorDialog.showError("Ошибка при сохранении отчета");
                    }
                }
            });
        });
    }

    private void addUserInfo(XWPFDocument document, String label, String value) {
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun();
        run.setText(label + " " + value);
        run.setFontSize(12);
    }
}


