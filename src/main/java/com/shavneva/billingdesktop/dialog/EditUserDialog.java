package com.shavneva.billingdesktop.dialog;

import com.shavneva.billingdesktop.entity.Role;
import com.shavneva.billingdesktop.entity.Tariff;
import com.shavneva.billingdesktop.entity.User;
import com.shavneva.billingdesktop.repository.CrudRepository;
import com.shavneva.billingdesktop.repository.factory.CrudFactory;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EditUserDialog extends Dialog<Boolean> {
    private TextField firstNameField;
    private TextField lastNameField;
    private TextField emailField;
    private TextField numberField;

    public EditUserDialog(User user) {
        setTitle("Редактировать пользователя");

        firstNameField = new TextField(user.getFirstName());
        lastNameField = new TextField(user.getLastName());
        emailField = new TextField(user.getEmail());
        numberField = new TextField(user.getNumber());

        // Создание сетки для расположения элементов управления
        GridPane grid = new GridPane();
        grid.add(new Label("Имя:"), 0, 0);
        grid.add(firstNameField, 1, 0);
        grid.add(new Label("Фамилия:"), 0, 1);
        grid.add(lastNameField, 1, 1);
        grid.add(new Label("Email:"), 0, 2);
        grid.add(emailField, 1, 2);
        grid.add(new Label("Номер телефона:"), 0, 3);
        grid.add(numberField, 1, 3);

        CrudRepository<Tariff> tariffCrudRepository = CrudFactory.createTariffRepository();
        List<Tariff> tariffList = tariffCrudRepository.getAll();
        ObservableList<Tariff> tariffObservableList = FXCollections.observableArrayList(tariffList);
        CrudRepository<Role> roleCrudRepository = CrudFactory.createRoleRepository();
        List<Role> roleList = roleCrudRepository.getAll();
        ObservableList<Role> roleObservableList = FXCollections.observableArrayList(roleList);

        ComboBox<Tariff> tariffComboBox = new ComboBox<>(tariffObservableList);
        tariffComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Tariff tariff) {
                return (tariff != null) ? tariff.getTariffName() : "Не указан";
            }

            @Override
            public Tariff fromString(String string) {
                return null;
            }
        });

        ComboBox<Role> roleComboBox = new ComboBox<>(roleObservableList);
        roleComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Role role) {
                return (role != null) ? role.getRoleName() : "Не указана";
            }

            @Override
            public Role fromString(String string) {
                return null;
            }
        });


        tariffComboBox.setPrefWidth(180);
        roleComboBox.setPrefWidth(180);


        grid.add(new Label("Тариф:"), 0, 4);
        grid.add(tariffComboBox, 1, 4);
        grid.add(new Label("Роль:"), 0, 5);
        grid.add(roleComboBox, 1, 5);

        // Установка выбранных значений ComboBox для пользователя
        Tariff userTariff = user.getTariff();
        if (userTariff != null) {
            tariffComboBox.setValue(userTariff);
        }

        Role userRole = user.getRoles().isEmpty() ? null : user.getRoles().get(0);
        if (userRole != null) {
            roleComboBox.setValue(userRole);
        }

        getDialogPane().setContent(grid);

        ButtonType saveButtonType = new ButtonType("Сохранить", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        setResultConverter(buttonType -> {
            if (buttonType == saveButtonType) {
                user.setFirstName(firstNameField.getText());
                user.setLastName(lastNameField.getText());
                user.setEmail(emailField.getText());
                user.setNumber(numberField.getText());
                user.setTariff(tariffComboBox.getValue());
                user.setRoles(Collections.singletonList(roleComboBox.getValue()));
                return true;
            }
            return false;
        });
    }
}