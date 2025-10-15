package org.example.telacad;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AlunoSolicitaController {
    @FXML
    private Button solicitarBtn;

    @FXML
    private Button HomeButton;

    @FXML
    private VBox sucessoDialog;

    @FXML
    private VBox mainPanel;

    // NOVO: Referência para o painel de overlay
    @FXML
    private VBox overlayPane;

    @FXML
    private void sucessoSolicitacao() {
        overlayPane.setVisible(true); // Mostra o fundo cinza
        sucessoDialog.setVisible(true);
        mainPanel.setDisable(true);
    }

    @FXML
    private void handleHomeButton() {
        // Ao navegar para a próxima tela, o overlay desaparecerá junto com a cena atual.
        // Não é preciso escondê-lo manualmente aqui.
        try {
            Stage stagePrincipal = (Stage) HomeButton.getScene().getWindow();
            Parent loginPage = FXMLLoader.load(getClass().getResource("/org/example/telacad/home-aluno.fxml"));
            Scene loginScene = new Scene(loginPage);
            stagePrincipal.setScene(loginScene);
            stagePrincipal.setTitle("Home_Aluno");
            stagePrincipal.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("ERRO: Falha ao carregar a Home Aluno.");
        }
    }

    @FXML
    private void handleBackToHome() {
        handleHomeButton();
    }

    @FXML
    private void voltar(ActionEvent event) throws IOException {
        Parent loginRoot = FXMLLoader.load(getClass().getResource("/org/example/telacad/home-aluno.fxml"));
        Scene loginScene = new Scene(loginRoot);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(loginScene);
        stage.show();
    }
}