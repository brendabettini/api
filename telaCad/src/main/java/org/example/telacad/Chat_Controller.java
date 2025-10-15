package org.example.telacad;

import java.io.IOException;

import javafx.application.Platform;
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
import javafx.stage.Stage;

public class Chat_Controller {

    // --- Variáveis do FXML ---
    // Componentes que linkamos com o fx:id
    @FXML
    private VBox chatVBox; // O container vertical para as mensagens

    @FXML
    private TextField messageTextField; // O campo para digitar o texto

    @FXML
    private ScrollPane chatScrollPane; // O painel de rolagem

    @FXML
    private Button voltarBtn;


    // --- Métodos de Ação ---

    /**
     * Este método é chamado quando o botão de enviar é clicado
     * ou quando a tecla Enter é pressionada no campo de texto.
     */
    @FXML
    private void sendMessage() {
        // 1. Pega o texto do campo de mensagem e remove espaços em branco extras
        String messageText = messageTextField.getText().trim();

        // 2. Verifica se a mensagem não está vazia
        if (!messageText.isEmpty()) {
            // 3. Cria a "bolha" da mensagem enviada
            addMessageBubble(messageText, true);

            // 4. Limpa o campo de texto para a próxima mensagem
            messageTextField.clear();

            // 5. Rola a tela para a mensagem mais recente
            // Usamos Platform.runLater para garantir que a rolagem aconteça
            // depois que a nova mensagem for adicionada à cena.
            Platform.runLater(() -> chatScrollPane.setVvalue(1.0));
        }
    }
    
    /**
     * Método auxiliar para criar e adicionar uma bolha de mensagem na tela.
     * @param message O texto da mensagem.
     * @param isSent 'true' se for uma mensagem enviada (alinhada à direita), 'false' se for recebida.
     */
    private void addMessageBubble(String message, boolean isSent) {
        // Cria o Label que vai conter o texto da mensagem
        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true); // Permite que o texto quebre a linha

        // Cria o HBox que vai segurar o Label
        HBox messageContainer = new HBox();

        if (isSent) {
            // Se for uma mensagem enviada
            messageLabel.getStyleClass().add("message-bubble-sent");
            messageContainer.setAlignment(Pos.CENTER_RIGHT); // Alinha à direita
        } else {
            // Se for uma mensagem recebida (você pode usar isso no futuro)
            messageLabel.getStyleClass().add("message-bubble-received");
            messageContainer.setAlignment(Pos.CENTER_LEFT); // Alinha à esquerda
        }
        
        // Adiciona o Label dentro do container HBox
        messageContainer.getChildren().add(messageLabel);
        
        // Adiciona o container da mensagem (HBox) no VBox principal do chat
        chatVBox.getChildren().add(messageContainer);
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
}