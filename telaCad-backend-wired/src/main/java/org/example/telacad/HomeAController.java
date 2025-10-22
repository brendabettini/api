package org.example.telacad;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class HomeAController {

    @FXML private Button perfilBtn;
    @FXML private Button documentButton;
    @FXML private Button selecionarBtn;
    @FXML private Button chatBtn;
    @FXML private Button voltarBtn;

    @FXML private VBox overlayPane;
    @FXML private VBox documentPopupVBox;
    @FXML private Label docEmptyMessageLabel;
    @FXML private ScrollPane docScrollPane;
    @FXML private VBox docListVBox;

    private static final String DOC_ICON_SVG = "M14,2H6A2,2 0 0,0 4,4V20A2,2 0 0,0 6,22H18A2,2 0 0,0 20,20V8L14,2M18,20H6V4H13V9H18V20Z";

    @FXML
    private void selecionar (ActionEvent event) throws IOException {
        if (event.getSource() instanceof Node sourceNode) {
            Parent AlunoSolicitaRoot = FXMLLoader.load(getClass().getResource("/org/example/telacad/Aluno-Solicita.fxml"));
            Scene AlunoSolicitaScene = new Scene(AlunoSolicitaRoot);
            Stage stage = (Stage) sourceNode.getScene().getWindow();
            stage.setScene(AlunoSolicitaScene);
            stage.setTitle("Solicitar Orientador");
            stage.setMaximized(true);
            stage.show();
        }
    }

    @FXML
    private void handleLoginButton (ActionEvent event) throws IOException {
        if (event.getSource() instanceof Node sourceNode) {
            Parent loginRoot = FXMLLoader.load(getClass().getResource("/org/example/telacad/LoginView.fxml"));
            Scene loginScene = new Scene(loginRoot);
            Stage stage = (Stage) sourceNode.getScene().getWindow();
            stage.setScene(loginScene);
            stage.setTitle("Login");
            stage.setMaximized(true);
            stage.show();
        }
    }

    @FXML
    private void abrir_Chat (ActionEvent event) throws IOException {
        if (event.getSource() instanceof Node sourceNode) {
            Parent chatRoot = FXMLLoader.load(getClass().getResource("/org/example/telacad/Chat-aluno.fxml"));
            Scene chatScene = new Scene(chatRoot);
            Stage stage = (Stage) sourceNode.getScene().getWindow();
            stage.setScene(chatScene);
            stage.setTitle("Chat com Professor");
            stage.setMaximized(true);
            stage.show();
        }
    }

    @FXML
    private void abrirPerfil() {
        try {
            if (perfilBtn == null || perfilBtn.getScene() == null) {
                System.err.println("ERRO: Botão de perfil ou sua cena não encontrados.");
                return;
            }
            Stage stagePrincipal = (Stage) perfilBtn.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/telacad/perfil_aluno.fxml"));
            Parent root = loader.load();
            Stage popupStage = new Stage();
            popupStage.initOwner(stagePrincipal);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initStyle(StageStyle.TRANSPARENT);
            popupStage.setMaximized(true);
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            popupStage.setScene(scene);
            popupStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("ERRO: Falha ao carregar a tela de perfil do aluno.");
        } catch (IllegalStateException e) {
            System.err.println("ERRO: Provável problema ao carregar FXML ou Controller do perfil: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleOpenDocuments(ActionEvent event) {
        List<String> documents;
        documents = new ArrayList<>();
        populateAndShowDocumentPopup(documents);
    }

    @FXML
    private void closeDocumentPopup() {
        if (documentPopupVBox != null) documentPopupVBox.setVisible(false);
        if (overlayPane != null) overlayPane.setVisible(false);
    }

    private void populateAndShowDocumentPopup(List<String> documents) {
        if (docListVBox == null || docEmptyMessageLabel == null || docScrollPane == null || overlayPane == null || documentPopupVBox == null) {
            System.err.println("Erro: Componentes do pop-up de documentos não foram injetados corretamente em HomeAController. Verifique os fx:ids.");
            return;
        }
        docListVBox.getChildren().clear();
        if (documents == null || documents.isEmpty()) {
            docEmptyMessageLabel.setVisible(true);
            docScrollPane.setVisible(false);
        } else {
            docEmptyMessageLabel.setVisible(false);
            docScrollPane.setVisible(true);
            for (String docName : documents) {
                docListVBox.getChildren().add(createDocumentItem(docName));
            }
        }
        overlayPane.setVisible(true);
        documentPopupVBox.setVisible(true);
    }

    private HBox createDocumentItem(String documentName) {
        SVGPath docIcon = new SVGPath();
        docIcon.setContent(DOC_ICON_SVG);
        docIcon.getStyleClass().add("document-icon-style");

        Label nameLabel = new Label(documentName);
        nameLabel.getStyleClass().add("document-name-style");

        HBox item = new HBox(10, docIcon, nameLabel);
        item.getStyleClass().add("document-item-style");
        item.setAlignment(Pos.CENTER_LEFT);

        item.setOnMouseClicked(event -> {
            System.out.println("Clicou para abrir: " + documentName);
        });
        return item;
    }
}