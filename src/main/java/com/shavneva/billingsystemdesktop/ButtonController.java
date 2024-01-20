package com.shavneva.billingsystemdesktop;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ButtonController {

    @FXML
    protected void openRegistrationWindowClick(ActionEvent event) {
        try {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.hide();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("registration-view.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 400, 290);
            Stage newStage = new Stage();
            newStage.hide();

            newStage.setTitle("Окно регистрации");
            newStage.setScene(scene);
            newStage.show();
            }catch (IOException e){
            ErrorDialog.showError("Произошла ошибка: " + e.getMessage());
        }
    }
    @FXML
    protected void openUserWindowClick(ActionEvent event) {
        try {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.hide();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("userwindow-view.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 400, 290);
            Stage newStage = new Stage();

            newStage.setTitle("Биллинговая система");
            newStage.setScene(scene);
            newStage.show();
        }catch (IOException e){
            ErrorDialog.showError("Произошла ошибка: " + e.getMessage());
        }
    }
}