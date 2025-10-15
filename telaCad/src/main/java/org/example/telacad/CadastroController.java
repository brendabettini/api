package org.example.telacad;

import java.io.FileWriter;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CadastroController {

    // --- Campos do Formulário ---
    @FXML private TextField nomeField;
    @FXML private TextField emailField;
    @FXML private TextField senhaField;
    @FXML private TextField confirmarField;
    @FXML private Button confirmBtn;
    @FXML private Button loginButton;

    // --- Componentes para a seleção de curso ---
    @FXML private Button cursoButton;
    @FXML private VBox cursoDialog;
    @FXML private ToggleGroup cursoToggleGroup;

    // --- Painéis que vamos controlar ---
    @FXML private VBox formContainer;
    @FXML private VBox sucessoDialog;

    // --- NOVO: Painel de overlay ---
    @FXML private VBox overlayPane;

    @FXML
    private void confirmarCadastro() {
        String nome = nomeField.getText();
        String curso = cursoButton.getText();
        String email = emailField.getText();
        String senha = senhaField.getText();

        // Salva os dados em arquivo...
        try (FileWriter writer = new FileWriter("dadosCastro.txt", true)) {
            writer.write(nome + "\n" + curso + "\n" + email + "\n" + senha + "\n");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Mostra o overlay e o diálogo de sucesso
        overlayPane.setVisible(true);
        sucessoDialog.setVisible(true);
        formContainer.setDisable(true); // Desabilita o formulário de fundo
    }

    @FXML
    private void handleCursoButton() {
        // Mostra o overlay e o seletor de curso
        overlayPane.setVisible(true);
        cursoDialog.setVisible(true);
        formContainer.setDisable(true); // Desabilita o formulário de fundo
    }

    @FXML
    private void handleOkCursoButton() {
        RadioButton selectedRadioButton = (RadioButton) cursoToggleGroup.getSelectedToggle();
        if (selectedRadioButton != null) {
            cursoButton.setText(selectedRadioButton.getText());
        }
        // Esconde o overlay e o seletor de curso
        overlayPane.setVisible(false);
        cursoDialog.setVisible(false);
        formContainer.setDisable(false); // Reabilita o formulário
    }

    @FXML
    private void handleLoginButton() {
        navigateToLogin();
    }

    @FXML
    private void handleBackToLogin() {
        navigateToLogin();
    }

    /**
     * Método centralizado para navegar para a tela de login.
     */
    private void navigateToLogin() {
        try {
            // Pega o Stage a partir de qualquer componente visível
            Stage stagePrincipal = (Stage) confirmBtn.getScene().getWindow();
            Parent loginPage = FXMLLoader.load(getClass().getResource("/org/example/telacad/LoginView.fxml"));
            Scene loginScene = new Scene(loginPage);
            stagePrincipal.setScene(loginScene);
            stagePrincipal.setTitle("Tela de Login");
            stagePrincipal.setMaximized(true);
            stagePrincipal.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("ERRO: Falha ao carregar a tela de login.");
        }
    }
}