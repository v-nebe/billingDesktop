package com.shavneva.billingdesktop;

import com.shavneva.billingdesktop.entity.*;
import com.shavneva.billingdesktop.repository.CrudRepository;
import com.shavneva.billingdesktop.repository.factory.CrudFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.List;

public class DataBaseLoader {
    public void showTableUser(ActionEvent event) {
        TableView<User> table = new TableView<>();

        TableColumn<User, String> firstNameColumn = new TableColumn<>("First Name");
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<User, String> lastNameColumn = new TableColumn<>("Last Name");
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<User, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<User, String> numberColumn = new TableColumn<>("Phone Number");
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));

        TableColumn<User, String> passwordColumn = new TableColumn<>("Password");
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));

        TableColumn<User, Integer> tariffIdColumn = new TableColumn<>("Tariff Id");
        tariffIdColumn.setCellValueFactory(new PropertyValueFactory<>("tariffId"));

        TableColumn<User, Integer> accountIdColumn = new TableColumn<>("Account Id");
        accountIdColumn.setCellValueFactory(new PropertyValueFactory<>("accountId"));


        table.getColumns().addAll(firstNameColumn, lastNameColumn, emailColumn, numberColumn, passwordColumn,
                tariffIdColumn, accountIdColumn);


        CrudRepository<User> userCrudRepository = CrudFactory.createUserRepository();
        List<User> userList = userCrudRepository.getAll();

        ObservableList<User> userObservableList = FXCollections.observableArrayList(userList);
        table.setItems(userObservableList);

        BorderPane root = new BorderPane();
        root.setCenter(table);
        Scene scene = new Scene(root, 800, 600);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Таблица Пользователи");
        stage.show();
    }
    public void showTableRoles(ActionEvent event) {
        TableView<Role> table = new TableView<>();

        TableColumn<Role, String> roleIdColumn = new TableColumn<>("Role Id");
        roleIdColumn.setCellValueFactory(new PropertyValueFactory<>("roleId"));

        TableColumn<Role, String> roleColumn = new TableColumn<>("Role");
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("roleName"));

        table.getColumns().addAll(roleIdColumn, roleColumn);

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

        TableColumn<Tariff, String> tariffIdColumn = new TableColumn<>("Tariff Id");
        tariffIdColumn.setCellValueFactory(new PropertyValueFactory<>("tariffId"));

        TableColumn<Tariff, String> tariffNameColumn = new TableColumn<>("Tariff Name");
        tariffNameColumn.setCellValueFactory(new PropertyValueFactory<>("tariffName"));

        TableColumn<Tariff, String> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));


        table.getColumns().addAll(tariffIdColumn, tariffNameColumn, priceColumn);


        CrudRepository<Tariff> tariffCrudRepository = CrudFactory.createTariffRepository();
        List<Tariff> tarriffList = tariffCrudRepository.getAll();

        ObservableList<Tariff> tariffObservableList = FXCollections.observableArrayList(tarriffList);
        table.setItems(tariffObservableList);

        BorderPane root = new BorderPane();
        root.setCenter(table);
        Scene scene = new Scene(root, 800, 600);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Таблица Тариффы");
        stage.show();
    }
    public void showTableServices(ActionEvent event) {
        TableView<Services> table = new TableView<>();

        TableColumn<Services, String> serviceIdColumn = new TableColumn<>("Service Id");
        serviceIdColumn.setCellValueFactory(new PropertyValueFactory<>("serviceId"));

        TableColumn<Services, String> priceColumn = new TableColumn<>("Service");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("service"));


        table.getColumns().addAll(serviceIdColumn, priceColumn);


        CrudRepository<Services> servicesCrudRepository = CrudFactory.createServicesRepository();
        List<Services> servicesList = servicesCrudRepository.getAll();

        ObservableList<Services> servicesObservableList = FXCollections.observableArrayList(servicesList);
        table.setItems(servicesObservableList);

        BorderPane root = new BorderPane();
        root.setCenter(table);
        Scene scene = new Scene(root, 800, 600);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Таблица Услуги");
        stage.show();
    }
    public void showTableAccount(ActionEvent event) {
        TableView<Account> table = new TableView<>();

        TableColumn<Account, String> accountIdColumn = new TableColumn<>("Account Id");
        accountIdColumn.setCellValueFactory(new PropertyValueFactory<>("accountId"));

        TableColumn<Account, String> amount = new TableColumn<>("Amount");
        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));


        table.getColumns().addAll(accountIdColumn, amount);


        CrudRepository<Account> accountCrudRepository = CrudFactory.createAccountRepository();
        List<Account> accountList = accountCrudRepository.getAll();

        ObservableList<Account> accountObservableList = FXCollections.observableArrayList(accountList);
        table.setItems(accountObservableList);

        BorderPane root = new BorderPane();
        root.setCenter(table);
        Scene scene = new Scene(root, 800, 600);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Таблица Счета");
        stage.show();
    }
}
