package org.example.telacad;

import org.example.telacad.db.UsuarioDAO;
import org.example.telacad.models.Usuario;

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

@FXML private TextField emailField;
@FXML private PasswordField passwordField; 
@FXML private Button loginButton;          
@FXML private Button cadastroBtn;

    private void trocarCena(ActionEvent event, String fxml, String titulo) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Scene scene = new Scene(root);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(titulo);
            var bounds = Screen.getPrimary().getVisualBounds();
            stage.setX(bounds.getMinX());
            stage.setY(bounds.getMinY());
            stage.setWidth(bounds.getWidth());
            stage.setHeight(bounds.getHeight());
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLoginAction(ActionEvent event) {
        String email = emailField != null ? emailField.getText() : "";
        String senha = passwordField != null ? passwordField.getText() : "";

        if (email.isBlank() || senha.isBlank()) {
            System.err.println("Preencha e-mail e senha.");
            return;
        }

        try {
            UsuarioDAO dao = new UsuarioDAO();
            Usuario u = dao.autenticar(email, senha);
            if (u == null) {
                System.err.println("E-mail ou senha inválidos.");
                return;
            }
            if (u.getStatus() == 0) {
                System.err.println("Usuário inativo.");
                return;
            }

            if (u.getPerfil() == 1) {
                trocarCena(event, "/org/example/telacad/Aluno-Solicita.fxml", "Solicitar Professor");
            } else if (u.getPerfil() == 2 || u.getPerfil() == 3) {
                trocarCena(event, "/org/example/telacad/home-professor.fxml", "Home Professor");
            } else {
                trocarCena(event, "/org/example/telacad/home-aluno.fxml", "Home Aluno");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleGoToCadastro(ActionEvent event) {
        trocarCena(event, "/org/example/telacad/Cadastro-all.fxml", "Cadastro");
    }
}
