package org.example.telacad;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class HomeAController {

    @FXML private Button perfilBtn;
    
    @FXML
    private void selecionar (ActionEvent event) throws IOException {
        Parent loginRoot = FXMLLoader.load(getClass().getResource("/org/example/telacad/Aluno-Solicita.fxml"));
        Scene loginScene = new Scene(loginRoot);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(loginScene);
        stage.show();
    }

     @FXML
    private void handleLoginButton (ActionEvent event) throws IOException {
        Parent loginRoot = FXMLLoader.load(getClass().getResource("/org/example/telacad/LoginView.fxml"));
        Scene loginScene = new Scene(loginRoot);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(loginScene);
        stage.show();
    }

    @FXML
    private void abrir_Chat (ActionEvent event) throws IOException {
        Parent loginRoot = FXMLLoader.load(getClass().getResource("/org/example/telacad/Chat-aluno.fxml"));
        Scene loginScene = new Scene(loginRoot);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(loginScene);
        stage.show();
    }

     // Este método deve estar no seu controller da tela HOME
@FXML
private void abrirPerfil() { // Troque pelo nome do seu método
    try {
        Stage stagePrincipal = (Stage) perfilBtn.getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/telacad/perfil_aluno.fxml"));
        Parent root = loader.load();

        Stage popupStage = new Stage();
        popupStage.initOwner(stagePrincipal);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initStyle(StageStyle.TRANSPARENT);

        // >>> NOVO: FAZ A JANELA DO POP-UP OCUPAR A TELA TODA <<<
        popupStage.setMaximized(true);

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        popupStage.setScene(scene);
        popupStage.showAndWait();

    } catch (IOException e) {
        e.printStackTrace();
    }
}
    
}
