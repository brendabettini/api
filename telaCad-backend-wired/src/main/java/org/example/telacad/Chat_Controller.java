package org.example.telacad;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Chat_Controller {

    @FXML private VBox chatVBox;
    @FXML private TextField messageTextField;
    @FXML private ScrollPane chatScrollPane;
    @FXML private Button voltarBtn;
    @FXML private Button perfilBtn;
    @FXML private Button chatDocButton;

    @FXML private VBox overlayPane;
    @FXML private VBox documentPopupVBox;
    @FXML private Label docEmptyMessageLabel;
    @FXML private ScrollPane docScrollPane;
    @FXML private VBox docListVBox;

    private static final String DOC_ICON_SVG = "M14,2H6A2,2 0 0,0 4,4V20A2,2 0 0,0 6,22H18A2,2 0 0,0 20,20V8L14,2M18,20H6V4H13V9H18V20Z";

    @FXML
    private void sendMessage() {
        if (messageTextField == null) return;
        String messageText = messageTextField.getText().trim();
        if (!messageText.isEmpty()) {
            addMessageBubble(messageText, true);
            messageTextField.clear();
            Platform.runLater(() -> {
                if(chatScrollPane != null) chatScrollPane.setVvalue(1.0);
            });
        }
    }

    private void addMessageBubble(String message, boolean isSent) {
        if (chatVBox == null) return;
        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        HBox messageContainer = new HBox();
        if (isSent) {
            messageLabel.getStyleClass().add("message-bubble-sent");
            messageContainer.setAlignment(Pos.CENTER_RIGHT);
        } else {
            messageLabel.getStyleClass().add("message-bubble-received");
            messageContainer.setAlignment(Pos.CENTER_LEFT);
        }
        messageContainer.getChildren().add(messageLabel);
        chatVBox.getChildren().add(messageContainer);
        Platform.runLater(() -> {
            if(chatScrollPane != null) chatScrollPane.setVvalue(1.0);
        });
    }

    @FXML
    private void voltar() {
        try {
            Stage stagePrincipal = (Stage) voltarBtn.getScene().getWindow();
            Parent loginPage = FXMLLoader.load(getClass().getResource("/org/example/telacad/home-aluno.fxml"));
            Scene loginScene = new Scene(loginPage);
            stagePrincipal.setScene(loginScene);
            stagePrincipal.setTitle("Home Aluno");
            stagePrincipal.setMaximized(true);
            stagePrincipal.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("ERRO: Falha ao carregar a tela de home.");
        }
    }

    @FXML
    private void abrirPerfil() {
        try {
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
        }
    }

    @FXML
    private void handleOpenDocuments(ActionEvent event) {
        List<String> documents;
        documents = Arrays.asList("Requisitos_Trabalho.docx", "Cronograma_Entrega.pdf");
        populateAndShowDocumentPopup(documents);
    }

    @FXML
    private void closeDocumentPopup() {
        if (documentPopupVBox != null) documentPopupVBox.setVisible(false);
        if (overlayPane != null) overlayPane.setVisible(false);
    }

    private void populateAndShowDocumentPopup(List<String> documents) {
        if (docListVBox == null || docEmptyMessageLabel == null || docScrollPane == null || overlayPane == null || documentPopupVBox == null) {
            System.err.println("Erro: Componentes do pop-up de documentos não foram injetados corretamente em Chat_Controller. Verifique os fx:ids em Chat-aluno.fxml.");
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