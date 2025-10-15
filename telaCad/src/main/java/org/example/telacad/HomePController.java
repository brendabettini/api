package org.example.telacad;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import javafx.application.Platform;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


enum TipoProfessor {
    TG,
    NORMAL
}

// Não se esqueça de ter a classe Aluno no seu projeto
class Aluno {
    private int id;
    private String nome;
    private String status;

    public Aluno(int id, String nome, String status) {
        this.id = id;
        this.nome = nome;
        this.status = status;
    }

    // Getters e Setters
    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}


public class HomePController {

    // --- INJEÇÕES FXML ---
    @FXML private VBox listaAlunosVBox;
    @FXML private VBox solicitacoesVBox;
    @FXML private VBox chatContainer;
    @FXML private Label chatAlunoNomeLabel;
    @FXML private Label emptyChatMessage;
    @FXML private ScrollPane messageScrollPane;
    @FXML private VBox messageVBox;
    @FXML private Button addUserButton;
    @FXML private Button sairBtn;
    @FXML private TextField messageInputField;
    @FXML private Button perfilBtn;

    // --- LISTA PARA GERENCIAR OS ALUNOS ATIVOS ---
    private List<Aluno> alunosAtivos = new ArrayList<>();

    public void configureViewFor(TipoProfessor tipo) {
        if (addUserButton != null) {
            if (tipo != TipoProfessor.TG) {
                addUserButton.setVisible(false);
                addUserButton.setManaged(false);
            }
        } else {
            System.out.println("AVISO: O botão 'addUserButton' não foi injetado pelo FXML.");
        }
    }

    @FXML
    public void initialize() {
        // A lista de alunos (esquerda) começa vazia
        atualizarListaAlunosUI();
        // A lista de solicitações (direita) começa com 3 itens para teste
        carregarSolicitacoes();

        messageInputField.setOnAction(event -> enviarMensagem());
    }

    private void atualizarListaAlunosUI() {
        listaAlunosVBox.getChildren().clear();
        for (Aluno aluno : alunosAtivos) {
            listaAlunosVBox.getChildren().add(criarItemAluno(aluno.getNome(), aluno.getStatus(), aluno.getId()));
        }
    }

    // --- MÉTODO AJUSTADO PARA CARREGAR 3 SOLICITAÇÕES INICIAIS ---
    private void carregarSolicitacoes() {
        solicitacoesVBox.getChildren().clear();
        // Cria 3 solicitações de exemplo com IDs de 1 a 3
        IntStream.rangeClosed(1, 3).forEach(i -> {
            solicitacoesVBox.getChildren().add(criarItemSolicitacao("Aluno " + i, i));
        });
    }

    private void handleAprovar(ActionEvent event) {
        int idAluno = (int) ((Node) event.getSource()).getUserData();
        String nomeAluno = "Aluno " + idAluno;
        System.out.println("Aprovando aluno: " + nomeAluno);

        boolean alunoJaExiste = alunosAtivos.stream().anyMatch(a -> a.getId() == idAluno);

        if (!alunoJaExiste) {
            Aluno novoAluno = new Aluno(idAluno, nomeAluno, "Sem novas mensagens");
            alunosAtivos.add(novoAluno);
            atualizarListaAlunosUI();
            removerSolicitacaoDaTela(idAluno);
            abrirChat(novoAluno.getNome(), novoAluno.getId());
        } else {
            System.out.println("Aluno ID " + idAluno + " já está na lista. Apenas removendo solicitação.");
            removerSolicitacaoDaTela(idAluno);
        }
    }
    
    // O restante do código permanece exatamente o mesmo da resposta anterior.
    // ...
    private void handleRecusar(ActionEvent event) {
        int idAluno = (int) ((Node) event.getSource()).getUserData();
        System.out.println("Recusando aluno ID: " + idAluno);
        removerSolicitacaoDaTela(idAluno);
    }

    @FXML
    private void abrirChat(String nomeAluno, int idAluno) {
        System.out.println("Abrindo chat para o aluno: " + nomeAluno);
        chatAlunoNomeLabel.setText(nomeAluno);
        messageVBox.getChildren().clear();

        // Lógica de exemplo para o conteúdo do chat
        emptyChatMessage.setVisible(false);
        messageScrollPane.setVisible(true);
        adicionarMensagem("Olá professor, tudo bem?", true);
        adicionarMensagem("Olá! Tudo sim. E com você?", false);
        
        chatContainer.setVisible(true);
        Platform.runLater(() -> messageScrollPane.setVvalue(1.0));
    }

    @FXML
    private void enviarMensagem() {
        String texto = messageInputField.getText();
        if (texto != null && !texto.trim().isEmpty()) {
            if (!messageScrollPane.isVisible()) {
                emptyChatMessage.setVisible(false);
                messageScrollPane.setVisible(true);
            }
            adicionarMensagem(texto, false);
            messageInputField.clear();
        }
    }

    private void adicionarMensagem(String texto, boolean isAluno) {
        Label bubble = new Label(texto);
        bubble.setWrapText(true);
        bubble.getStyleClass().add("message-bubble");
        bubble.getStyleClass().add(isAluno ? "student-bubble" : "professor-bubble");

        HBox messageRow = new HBox(bubble);
        messageRow.setAlignment(isAluno ? Pos.CENTER_LEFT : Pos.CENTER_RIGHT);
        messageVBox.getChildren().add(messageRow);

        Platform.runLater(() -> messageScrollPane.setVvalue(1.0));
    }

    @FXML
    void fecharChat(ActionEvent event) {
        if (chatContainer != null) {
            chatContainer.setVisible(false);
        }
    }

    private void removerSolicitacaoDaTela(int idAluno) {
        solicitacoesVBox.getChildren().removeIf(node -> node.getUserData() != null && (int) node.getUserData() == idAluno);
    }

    private HBox criarItemAluno(String nome, String status, int id) {
        StackPane circleContainer = new StackPane(new Circle(22, Color.web("#E0E0E0")));
        circleContainer.setPrefSize(44, 44);

        Label labelNome = new Label(nome);
        labelNome.getStyleClass().add("aluno-name");

        Label labelStatus = new Label(status);
        labelStatus.getStyleClass().add("aluno-status");

        VBox textVBox = new VBox(labelNome, labelStatus);
        textVBox.setAlignment(Pos.CENTER_LEFT);

        HBox item = new HBox(15, circleContainer, textVBox);
        item.getStyleClass().add("list-item");
        item.setAlignment(Pos.CENTER_LEFT);
        item.setOnMouseClicked(event -> abrirChat(nome, id));

        return item;
    }

    private VBox criarItemSolicitacao(String nomeAluno, int idAluno) {
        StackPane circleContainer = new StackPane(new Circle(30, Color.web("#E0E0E0")));
        circleContainer.setPrefSize(60, 60);

        Label labelNome = new Label(nomeAluno);
        labelNome.getStyleClass().add("solicitacao-aluno-name");

        HBox topRow = new HBox(15, circleContainer, labelNome);
        topRow.setAlignment(Pos.CENTER_LEFT);
        topRow.setPadding(new javafx.geometry.Insets(0, 0, 10, 0));

        Button btnAprovar = new Button("APROVAR");
        btnAprovar.getStyleClass().add("btn-aprovar");
        btnAprovar.setUserData(idAluno);
        btnAprovar.setOnAction(this::handleAprovar);

        Button btnRecusar = new Button("RECUSAR");
        btnRecusar.getStyleClass().add("btn-recusar");
        btnRecusar.setUserData(idAluno);
        btnRecusar.setOnAction(this::handleRecusar);

        HBox hboxBotos = new HBox(10, btnAprovar, btnRecusar);
        hboxBotos.setAlignment(Pos.CENTER);

        VBox itemBox = new VBox(5, topRow, hboxBotos);
        itemBox.getStyleClass().add("solicitacao-item");
        itemBox.setAlignment(Pos.CENTER);
        itemBox.setUserData(idAluno);

        return itemBox;
    }
    
    @FXML
    private void sair() {
        try {
            Stage stagePrincipal = (Stage) sairBtn.getScene().getWindow();
            Parent loginPage = FXMLLoader.load(getClass().getResource("/org/example/telacad/LoginView.fxml"));
            Scene loginScene = new Scene(loginPage);
            stagePrincipal.setScene(loginScene);
            stagePrincipal.setTitle("Home Professor");
            stagePrincipal.setMaximized(true);
            stagePrincipal.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("ERRO: Falha ao carregar a tela de home.");
        }
    }

    @FXML
    private void addProf() {
        try {
            Stage stagePrincipal = (Stage) addUserButton.getScene().getWindow();
            Parent loginPage = FXMLLoader.load(getClass().getResource("/org/example/telacad/Cadastro-prof.fxml"));
            Scene loginScene = new Scene(loginPage);
            stagePrincipal.setScene(loginScene);
            stagePrincipal.setTitle("Cadastro de Professor");
            stagePrincipal.setMaximized(true);
            stagePrincipal.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("ERRO: Falha ao carregar a tela de cadastro.");
        }
    }

    @FXML
    private void abrirPerfil() { // Renomeei para seguir a convenção do onAction
        try {
            // Carrega o FXML da tela de perfil
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/telacad/Perfil_Professor.fxml"));
            Parent root = loader.load();

            // 1. Pega a janela principal (a que está por trás)
            Stage ownerStage = (Stage) perfilBtn.getScene().getWindow();

            // Cria a nova janela para o pop-up
            Stage popupStage = new Stage();
            popupStage.initStyle(StageStyle.TRANSPARENT);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initOwner(ownerStage);

            // 2. CORREÇÃO PRINCIPAL: Use as dimensões da janela principal ao criar a nova Scene
            Scene scene = new Scene(root, ownerStage.getWidth(), ownerStage.getHeight());
            scene.setFill(Color.TRANSPARENT);

            // Define a cena e mostra a janela
            popupStage.setScene(scene);
            popupStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("ERRO: Falha ao carregar a tela de perfil.");
        }
}
}