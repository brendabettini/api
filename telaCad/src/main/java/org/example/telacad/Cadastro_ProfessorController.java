package org.example.telacad;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;

public class Cadastro_ProfessorController {

    // --- Injeções FXML dos botões e painéis ---
    @FXML private Button voltarBtn;
    @FXML private Button cadButton;
    @FXML private Button confirmBtn;
    @FXML private VBox sucessoDialog;
    @FXML private TilePane professoresTilePane;

    // --- Injeções FXML dos campos do formulário ---
    @FXML private TextField nomeCompletoField;
    @FXML private TextField emailField;
    @FXML private PasswordField senhaField;
    @FXML private PasswordField confirmarSenhaField;

    // --- Injeções para o seletor de cursos ---
    @FXML private Button cursoButton;
    @FXML private VBox cursoDialog;
    @FXML private VBox cursosContainer;

    // --- Injeção para o painel de overlay ---
    @FXML private VBox overlayPane;

    // --- Variáveis de estado ---
    private String nomeProfessorCadastrado;
    private List<String> cursosSelecionados = new ArrayList<>();

    // --- NOVO: Lista de cursos que será exibida no pop-up ---
    private final List<String> TODOS_OS_CURSOS = Arrays.asList(
            "Análise e Desenvolvimento de Sistemas",
            "Banco de Dados",
            "Desenvolvimento de Software Multiplataforma",
            "Gestão da Produção Industrial",
            "Gestão Empresarial",
            "Logística",
            "Manufatura Avançada",
            "Manutenção de Aeronaves",
            "Projetos de Estruturas Aeronáuticas"
    );

    /**
     * ADICIONADO: Este método faltava. Ele é executado assim que a tela é carregada
     * e é responsável por criar os checkboxes dos cursos.
     */
    @FXML
    public void initialize() {
        // Popula a lista de cursos com checkboxes dinamicamente
        for (String nomeCurso : TODOS_OS_CURSOS) {
            CheckBox checkBox = new CheckBox(nomeCurso);
            checkBox.getStyleClass().add("curso-checkbox"); // Adiciona uma classe de estilo
            cursosContainer.getChildren().add(checkBox);
        }
    }


    // --- Métodos de Navegação e Ações Principais ---

    @FXML
    private void voltarHome() {
        try {
            Stage stagePrincipal = (Stage) voltarBtn.getScene().getWindow();
            Parent loginPage = FXMLLoader.load(getClass().getResource("/org/example/telacad/home-professor.fxml"));
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
    private void cadastrarProf() {
        String nome = nomeCompletoField.getText();
        if (nome != null && !nome.trim().isEmpty()) {
            this.nomeProfessorCadastrado = nome;
            overlayPane.setVisible(true); // Mostra o overlay
            sucessoDialog.setVisible(true); // Mostra o diálogo de sucesso
        } else {
            System.out.println("O campo nome não pode estar vazio.");
        }
    }

    @FXML
    private void handleCadButton() {
        overlayPane.setVisible(false); // Esconde o overlay
        sucessoDialog.setVisible(false); // Esconde o diálogo de sucesso

        System.out.println("Professor: " + this.nomeProfessorCadastrado + " - Cursos: " + cursosSelecionados);

        Button novoProfessorBtn = criarBotaoProfessor(this.nomeProfessorCadastrado);
        professoresTilePane.getChildren().add(novoProfessorBtn);
        limparFormulario();
    }

    // --- MÉTODOS PARA O SELETOR DE CURSOS ---

    @FXML
    private void handleCursoButton() {
        overlayPane.setVisible(true); // Mostra o overlay
        cursoDialog.setVisible(true); // Mostra o diálogo de cursos
    }

    @FXML
    private void handleOkCursoButton() {
        this.cursosSelecionados = cursosContainer.getChildren().stream()
                .filter(node -> node instanceof CheckBox && ((CheckBox) node).isSelected())
                .map(node -> ((CheckBox) node).getText())
                .collect(Collectors.toList());

        if (cursosSelecionados.isEmpty()) {
            cursoButton.setText("Selecione o curso");
        } else {
            cursoButton.setText(String.join(", ", cursosSelecionados));
        }

        overlayPane.setVisible(false); // Esconde o overlay
        cursoDialog.setVisible(false); // Esconde o diálogo de cursos
    }


    // --- Métodos Auxiliares ---

    private void limparFormulario() {
        nomeCompletoField.clear();
        emailField.clear();
        senhaField.clear();
        confirmarSenhaField.clear();
        cursoButton.setText("Selecione o curso");
        cursosSelecionados.clear();

        for (Node node : cursosContainer.getChildren()) {
            if (node instanceof CheckBox) {
                ((CheckBox) node).setSelected(false);
            }
        }
    }

    private Button criarBotaoProfessor(String nome) {
        Circle profileCircle = new Circle(18.0);
        profileCircle.getStyleClass().add("profile-icon");
        SVGPath profileIcon = new SVGPath();
        profileIcon.setContent("M12,12A5,5 0 0,1 7,7A5,5 0 0,1 12,2A5,5 0 0,1 17,7A5,5 0 0,1 12,12M12,14C16.42,14 20,15.79 20,18V20H4V18C4,15.79 7.58,14 12,14Z");
        profileIcon.getStyleClass().add("nav-icon");
        StackPane iconContainer = new StackPane(profileCircle, profileIcon);
        iconContainer.setAlignment(Pos.CENTER);
        Label nomeLabel = new Label(nome);
        HBox graphicBox = new HBox(10, iconContainer, nomeLabel);
        graphicBox.setAlignment(Pos.CENTER_LEFT);
        Button professorButton = new Button();
        professorButton.setGraphic(graphicBox);
        professorButton.getStyleClass().add("professor-button");
        professorButton.setPrefWidth(280);
        return professorButton;
    }
}