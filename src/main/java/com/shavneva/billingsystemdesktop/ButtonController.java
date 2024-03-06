package com.shavneva.billingsystemdesktop;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.*;



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

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("registration-view.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 400, 290);
            Stage newStage = new Stage();
            newStage.hide();

            newStage.setTitle("Окно регистрации");
            newStage.setScene(scene);
            newStage.show();

            } catch(IOException e){
            ErrorDialog.showError("Произошла ошибка: " + e.getMessage());
        }
    }
    @FXML
    protected void openUWindowClick(ActionEvent event){
        try {
            String firstName = name.getText();
            String lastName = surname.getText();
            String registrationEmail = email.getText();
            String pNumber =  phoneNumber.getText();
            String password = passwordField.getText();
            String rPassword = repeatedPassword.getText();

            String requestBody = "{\"firstName\""+":"+"\""+firstName+"\""+","+
                    "\"lastName\""+":"+"\""+lastName+"\""+","+
                    "\"email\""+":"+"\""+registrationEmail+"\""+","+
                    "\"number\""+":"+"\""+pNumber+"\""+","+
                    "\"password\""+":"+"\""+password+"\""+
                    "}";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/users/createUser"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.statusCode());
            System.out.println(response.body());

            if(response.statusCode() == 200){
                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentStage.hide();

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("userwindow-view.fxml"));

                Scene scene = new Scene(fxmlLoader.load(), 400, 290);
                Stage newStage = new Stage();

                newStage.setTitle("Биллинговая система");
                newStage.setScene(scene);
                newStage.show();
            }
        } catch (IOException e){
            ErrorDialog.showError("Произошла ошибка: " + e.getMessage());
        } catch (URISyntaxException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void openUserWindowClick(ActionEvent event) {
        try{
            String userName = login.getText();
            String password = passwordField.getText();

            String requestBody = "{\"login\""+":"+"\""+userName+"\""+","+"\"password\""+":"+"\""+password+"\""+
                    "}";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/users/login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.statusCode());
            System.out.println(response.body());

            if(response.statusCode() == 200){
                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentStage.hide();

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("userwindow-view.fxml"));

                Scene scene = new Scene(fxmlLoader.load(), 400, 290);
                Stage newStage = new Stage();

                newStage.setTitle("Биллинговая система");
                newStage.setScene(scene);
                newStage.show();
            }
           /* else{
                JSONObject json = new JSONObject(response.body());
                ErrorDialog.showError("Произошла ошибка: " + json.get);
            }*/
        } catch (IOException e){
            ErrorDialog.showError("Произошла ошибка: " + e.getMessage());
        } catch (URISyntaxException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}