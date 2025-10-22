package org.example.telacad;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
import javafx.scene.shape.SVGPath;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

enum TipoProfessor {
    TG,
    NORMAL
}

class Aluno {
    private int id;
    private String nome;
    private String status;
    public Aluno(int id, String nome, String status) { this.id = id; this.nome = nome; this.status = status; }
    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

public class HomePController {

    @FXML private VBox listaAlunosVBox;
    @FXML private VBox solicitacoesVBox;
    @FXML private Button addUserButton;
    @FXML private Button sairBtn;
    @FXML private Button perfilBtn;
    @FXML private Button docNavButton;

    @FXML private VBox chatContainer;
    @FXML private Label chatAlunoNomeLabel;
    @FXML private Label emptyChatMessage;
    @FXML private ScrollPane messageScrollPane;
    @FXML private VBox messageVBox;
    @FXML private TextField messageInputField;

    @FXML private VBox overlayPane;
    @FXML private VBox documentPopupVBox;
    @FXML private Label docEmptyMessageLabel;
    @FXML private ScrollPane docScrollPane;
    @FXML private VBox docListVBox;

    private List<Aluno> alunosAtivos = new ArrayList<>();

    private static final String DOC_ICON_SVG = "M14,2H6A2,2 0 0,0 4,4V20A2,2 0 0,0 6,22H18A2,2 0 0,0 20,20V8L14,2M18,20H6V4H13V9H18V20Z";

    public void configureViewFor(TipoProfessor tipo) {
        if (addUserButton != null) {
            if (tipo != TipoProfessor.TG) {
                addUserButton.setVisible(false);
                addUserButton.setManaged(false);
            } else {
                addUserButton.setVisible(true);
                addUserButton.setManaged(true);
            }
        } else {
            System.out.println("AVISO: O botão 'addUserButton' não foi injetado pelo FXML em HomePController.");
        }
    }

    @FXML
    public void initialize() {
        atualizarListaAlunosUI();
        carregarSolicitacoes();
        if (messageInputField != null) {
            messageInputField.setOnAction(this::enviarMensagem);
        } else {
            System.err.println("WARN: messageInputField não foi injetado.");
        }
    }

    private void atualizarListaAlunosUI() {
        if (listaAlunosVBox == null) return;
        listaAlunosVBox.getChildren().clear();
        for (Aluno aluno : alunosAtivos) {
            listaAlunosVBox.getChildren().add(criarItemAluno(aluno.getNome(), aluno.getStatus(), aluno.getId()));
        }
    }

    private void carregarSolicitacoes() {
        if (solicitacoesVBox == null) return;
        solicitacoesVBox.getChildren().clear();
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
            System.out.println("Aluno ID " + idAluno + " já está na lista.");
            removerSolicitacaoDaTela(idAluno);
        }
    }

    private void handleRecusar(ActionEvent event) {
        int idAluno = (int) ((Node) event.getSource()).getUserData();
        System.out.println("Recusando aluno ID: " + idAluno);
        removerSolicitacaoDaTela(idAluno);
    }

    @FXML
    private void abrirChat(String nomeAluno, int idAluno) {
        if (chatContainer == null || chatAlunoNomeLabel == null || messageVBox == null || emptyChatMessage == null || messageScrollPane == null) {
            System.err.println("Erro: Componentes do chat não injetados."); return;
        }
        System.out.println("Abrindo chat para o aluno: " + nomeAluno);
        chatAlunoNomeLabel.setText(nomeAluno);
        messageVBox.getChildren().clear();
        emptyChatMessage.setVisible(false);
        messageScrollPane.setVisible(true);
        adicionarMensagem("Olá professor, tudo bem?", true);
        adicionarMensagem("Olá! Tudo sim. E com você?", false);

        if (overlayPane != null) overlayPane.setVisible(true);
        chatContainer.setVisible(true);
        Platform.runLater(() -> messageScrollPane.setVvalue(1.0));
    }

    @FXML
    void enviarMensagem(ActionEvent event) {
        if (messageInputField == null) return;
        String texto = messageInputField.getText().trim();
        if (!texto.isEmpty()) {
            if (!messageScrollPane.isVisible()) {
                emptyChatMessage.setVisible(false);
                messageScrollPane.setVisible(true);
            }
            adicionarMensagem(texto, false);
            messageInputField.clear();
        }
    }

    private void adicionarMensagem(String texto, boolean isAluno) {
        if (messageVBox == null || messageScrollPane == null) return;
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
        if (chatContainer != null) chatContainer.setVisible(false);
        if (overlayPane != null) overlayPane.setVisible(false);
    }

    private void removerSolicitacaoDaTela(int idAluno) {
        if (solicitacoesVBox != null) {
            solicitacoesVBox.getChildren().removeIf(node -> node.getUserData() != null && (int) node.getUserData() == idAluno);
        }
    }

    private HBox criarItemAluno(String nome, String status, int id) {
        StackPane circleContainer = new StackPane(new Circle(22, Color.web("#E0E0E0")));
        circleContainer.setPrefSize(44, 44);
        Label labelNome = new Label(nome);
        labelNome.getStyleClass().add("aluno-name");
        Label labelStatus = new Label(status);
        labelStatus.getStyleClass().add("aluno-status");
        HBox hboxText = new HBox(8, labelNome, labelStatus);
        hboxText.setAlignment(Pos.CENTER_LEFT);
        HBox item = new HBox(15, circleContainer, hboxText);
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
    private void handleOpenDocuments(ActionEvent event) {
        List<String> documents;
        documents = Arrays.asList("Documento_Geral_1.pdf", "Aviso_Importante.docx");
        populateAndShowDocumentPopup(documents);
    }

    @FXML
    private void closeDocumentPopup() {
        if (documentPopupVBox != null) documentPopupVBox.setVisible(false);
        if (overlayPane != null) overlayPane.setVisible(false);
    }

    private void populateAndShowDocumentPopup(List<String> documents) {
        if (docListVBox == null || docEmptyMessageLabel == null || docScrollPane == null || overlayPane == null || documentPopupVBox == null) {
            System.err.println("Erro: Componentes do pop-up de documentos não foram injetados corretamente em HomePController.");
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

    @FXML
    private void sair(ActionEvent event) {
        try {
            Stage stagePrincipal = (Stage) sairBtn.getScene().getWindow();
            Parent loginPage = FXMLLoader.load(getClass().getResource("/org/example/telacad/LoginView.fxml"));
            Scene loginScene = new Scene(loginPage);
            stagePrincipal.setScene(loginScene);
            stagePrincipal.setTitle("Login");
            stagePrincipal.setMaximized(true);
            stagePrincipal.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("ERRO: Falha ao carregar a tela de login.");
        }
    }

    @FXML
    private void addProf(ActionEvent event) {
        try {
            Stage stagePrincipal = (Stage) addUserButton.getScene().getWindow();
            Parent cadProfPage = FXMLLoader.load(getClass().getResource("/org/example/telacad/Cadastro-prof.fxml"));
            Scene cadProfScene = new Scene(cadProfPage);
            stagePrincipal.setScene(cadProfScene);
            stagePrincipal.setTitle("Cadastro de Professor");
            stagePrincipal.setMaximized(true);
            stagePrincipal.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("ERRO: Falha ao carregar a tela de cadastro.");
        }
    }

    @FXML
    private void abrirPerfil(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/telacad/Perfil_Professor.fxml"));
            Parent root = loader.load();
            Stage ownerStage = (Stage) perfilBtn.getScene().getWindow();
            Stage popupStage = new Stage();
            popupStage.initStyle(StageStyle.TRANSPARENT);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initOwner(ownerStage);
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            popupStage.setScene(scene);
            popupStage.setWidth(ownerStage.getWidth() * 0.8);
            popupStage.setHeight(ownerStage.getHeight() * 0.8);
            popupStage.centerOnScreen();
            popupStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("ERRO: Falha ao carregar a tela de perfil.");
        }
    }
}