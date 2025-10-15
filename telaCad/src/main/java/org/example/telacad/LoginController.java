package org.example.telacad;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    void handleLoginAction(ActionEvent event) throws IOException  {


        Parent homeRoot = FXMLLoader.load(getClass().getResource("/org/example/telacad/home-aluno.fxml"));
        Scene homeScene = new Scene(homeRoot);

        // pegar a janela atual
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(homeScene);
        var bounds = Screen.getPrimary().getVisualBounds();
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
        stage.show();

        /*String email = emailField.getText();
        String senha = passwordField.getText();

        if (email.isEmpty() || senha.isEmpty()) {
            System.out.println("Por favor, preencha todos os campos.");

        } else {
            System.out.println("Tentativa de login com:");
            System.out.println("E-mail: " + email);
            System.out.println("Senha: " + "");
        }*/

    }

    @FXML
    private void handleGoToCadastro(ActionEvent event) throws IOException {
        Parent cadastroRoot = FXMLLoader.load(getClass().getResource("/org/example/telacad/Cadastro-all.fxml"));
        Scene cadastroScene = new Scene(cadastroRoot);

        // pegar a janela atual
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(cadastroScene);
        var bounds = Screen.getPrimary().getVisualBounds();
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
        stage.show();
    }
}
