package org.example.telacad;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javafx.event.ActionEvent; // Adicionado para handleSuccessOk e outros se necessário
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;

public class Vizualizar_ProfessorController {

    // Record Aluno precisa ser movido para um arquivo próprio ou definido aqui se usado apenas aqui.
    private record Aluno(String nome, String status) {}
    private static final String USER_ICON_SVG = "M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z";

    @FXML private TextField situationTextField;
    @FXML private Button toggleStatusButton;
    @FXML private TextField nomeCompletoField;
    @FXML private TextField emailField;
    @FXML private Pane overlayPane; // Mantido para cursoDialog e successDialog
    @FXML private VBox cursoDialog;
    @FXML private VBox cursosContainer;
    @FXML private TilePane studentGrid;
    @FXML private FlowPane cursosSelecionadosContainer;
    @FXML private Button saveButton;
    @FXML private VBox successDialog;

    private boolean isProfessorActive = true;
    private List<String> cursosSelecionados = new ArrayList<>();
    private final List<String> TODOS_OS_CURSOS = Arrays.asList(
            "Análise e Desenvolvimento de Sistemas", "Banco de Dados",
            "Desenvolvimento de Software Multiplataforma", "Gestão da Produção Industrial",
            "Gestão Empresarial", "Logística", "Manufatura Avançada",
            "Manutenção de Aeronaves", "Projetos de Estruturas Aeronáuticas"
    );

    private String savedNome = "";
    private String savedEmail = "";
    private List<String> savedCursos = new ArrayList<>();

    @FXML
    public void initialize() {
        setFieldsDisabled(false);
        if(situationTextField!=null) situationTextField.setText("Ativa");
        if(toggleStatusButton!=null) {
            toggleStatusButton.setText("DESATIVAR");
            toggleStatusButton.getStyleClass().setAll("deactivate-button");
        }

        if (cursosContainer != null) {
            for (String nomeCurso : TODOS_OS_CURSOS) {
                CheckBox checkBox = new CheckBox(nomeCurso);
                checkBox.getStyleClass().add("curso-checkbox");
                checkBox.setWrapText(true);
                cursosContainer.getChildren().add(checkBox);
            }
        } else {
            System.err.println("WARN: cursosContainer não injetado em Vizualizar_ProfessorController.");
        }


        saveCurrentState();
        if (nomeCompletoField != null) nomeCompletoField.textProperty().addListener((obs, oldVal, newVal) -> checkForChanges());
        if (emailField != null) emailField.textProperty().addListener((obs, oldVal, newVal) -> checkForChanges());

        popularGridAlunos();
        atualizarDisplayCursos();
    }

    private void checkForChanges() {
        boolean nomeChanged = !nomeCompletoField.getText().equals(savedNome);
        boolean emailChanged = !emailField.getText().equals(savedEmail);
        boolean cursosChanged = !new HashSet<>(cursosSelecionados).equals(new HashSet<>(savedCursos));
        boolean hasUnsavedChanges = nomeChanged || emailChanged || cursosChanged;
        if(saveButton != null) saveButton.setVisible(hasUnsavedChanges);
    }

    private void saveCurrentState() {
        if(nomeCompletoField != null) this.savedNome = nomeCompletoField.getText();
        if(emailField != null) this.savedEmail = emailField.getText();
        this.savedCursos = new ArrayList<>(cursosSelecionados);
    }

    @FXML
    private void handleSaveChanges() {
        System.out.println("Alterações salvas!");
        saveCurrentState();
        if(saveButton != null) saveButton.setVisible(false);
        if(overlayPane != null) overlayPane.setVisible(true);
        if(successDialog != null) successDialog.setVisible(true);
    }

    @FXML
    private void handleSuccessOk(ActionEvent event) { // Adicionado ActionEvent se o FXML chamar assim
        if(overlayPane != null) overlayPane.setVisible(false);
        if(successDialog != null) successDialog.setVisible(false);
    }

    private void atualizarDisplayCursos() {
        if (cursosSelecionadosContainer == null) return;
        cursosSelecionadosContainer.getChildren().clear();
        if (cursosSelecionados.isEmpty()) {
            Label placeholder = new Label("Nenhum curso selecionado");
            placeholder.getStyleClass().add("placeholder-label");
            cursosSelecionadosContainer.getChildren().add(placeholder);
        } else {
            for (String nomeCurso : cursosSelecionados) {
                Label cursoTag = new Label(nomeCurso);
                cursoTag.getStyleClass().add("curso-tag");
                cursosSelecionadosContainer.getChildren().add(cursoTag);
            }
        }
    }

    @FXML
    private void handleOkCursoButton(ActionEvent event) { // Adicionado ActionEvent
        if (cursosContainer == null) return;
        this.cursosSelecionados = cursosContainer.getChildren().stream()
                .filter(node -> node instanceof CheckBox && ((CheckBox) node).isSelected())
                .map(node -> ((CheckBox) node).getText())
                .collect(Collectors.toList());
        atualizarDisplayCursos();
        checkForChanges();
        handleCloseCursoButton(); // Chama o método para fechar
    }

    @FXML
    private void handleAbrirSelecaoCursos() {
        updateCheckboxes();
        if(overlayPane != null) overlayPane.setVisible(true);
        if(cursoDialog != null) cursoDialog.setVisible(true);
    }

    private void setFieldsDisabled(boolean isDisabled) {
        if(nomeCompletoField != null) nomeCompletoField.setDisable(isDisabled);
        if(emailField != null) emailField.setDisable(isDisabled);
        if(cursosSelecionadosContainer != null) cursosSelecionadosContainer.setDisable(isDisabled);
    }

    private void popularGridAlunos() {
        if (studentGrid == null) return;
        List<Aluno> alunos = Arrays.asList(
                new Aluno("Aluno 1", "Não Iniciado"), new Aluno("Aluno 2", "Não Iniciado"), new Aluno("Aluno 3", "Não Iniciado"),
                new Aluno("Aluno 4", "Em Andamento"), new Aluno("Aluno 5", "Em Andamento"), new Aluno("Aluno 6", "Em Andamento"),
                new Aluno("Aluno 7", "Em Andamento"), new Aluno("Aluno 8", "Em Andamento"), new Aluno("Aluno 9", "Em Andamento"),
                new Aluno("Aluno 10", "Concluído"), new Aluno("Aluno 11", "Concluído"), new Aluno("Aluno 12", "Concluído"),
                new Aluno("Aluno 13", "Desistente"), new Aluno("Aluno 14", "Desistente")
        );
        studentGrid.getChildren().clear();
        for (Aluno aluno : alunos) {
            HBox studentCard = new HBox();
            studentCard.getStyleClass().add("student-card");
            switch (aluno.status()) {
                case "Em Andamento" -> studentCard.getStyleClass().add("status-in-progress");
                case "Concluído" -> studentCard.getStyleClass().add("status-completed");
                case "Desistente" -> studentCard.getStyleClass().add("status-dropout");
                default -> studentCard.getStyleClass().add("status-not-started");
            }
            studentCard.getChildren().addAll(createUserIcon(), new Label(aluno.nome()));
            studentGrid.getChildren().add(studentCard);
        }
    }

    private Node createUserIcon() {
        SVGPath iconPath = new SVGPath();
        iconPath.setContent(USER_ICON_SVG);
        iconPath.getStyleClass().add("student-icon-path"); // Verifique se essa classe existe no CSS
        StackPane iconContainer = new StackPane(iconPath);
        iconContainer.setPrefSize(24, 24);
        iconPath.setScaleX(0.7);
        iconPath.setScaleY(0.7);
        Circle background = new Circle(18);
        background.getStyleClass().add("student-icon-background"); // Verifique se essa classe existe no CSS
        return new StackPane(background, iconContainer);
    }

    @FXML
    private void handleToggleStatus() {
        isProfessorActive = !isProfessorActive;
        if(situationTextField != null) situationTextField.setText(isProfessorActive ? "Ativa" : "Inativa");
        if(toggleStatusButton != null) {
            toggleStatusButton.setText(isProfessorActive ? "DESATIVAR" : "ATIVAR");
            toggleStatusButton.getStyleClass().setAll(isProfessorActive ? "deactivate-button" : "activate-button");
        }
        setFieldsDisabled(!isProfessorActive);
    }

    // Método para fechar o diálogo de cursos (chamado pelo handleOkCursoButton)
    private void handleCloseCursoButton() {
        if(overlayPane != null) overlayPane.setVisible(false);
        if(cursoDialog != null) cursoDialog.setVisible(false);
    }


    private void updateCheckboxes() {
        if (cursosContainer == null) return;
        cursosContainer.getChildren().forEach(node -> {
            if (node instanceof CheckBox checkBox) {
                checkBox.setSelected(cursosSelecionados.contains(checkBox.getText()));
            }
        });
    }

    @FXML
    private void fechar() {
        try {
            // Tenta pegar o Stage a partir do overlayPane, ou outro componente se overlayPane for nulo
            Node sourceNode = overlayPane != null ? overlayPane : (saveButton != null ? saveButton : nomeCompletoField);
            if (sourceNode == null || sourceNode.getScene() == null) {
                System.err.println("Não foi possível obter a cena para fechar a janela.");
                return;
            }
            Stage stage = (Stage) sourceNode.getScene().getWindow();

            // Volta para Cadastro-prof.fxml (ajuste se a tela anterior for outra)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/telacad/Cadastro-prof.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(true); // Mantém maximizado
            stage.show();
        } catch (IOException e) {
            System.err.println("Erro ao carregar a tela de cadastro de professor:");
            e.printStackTrace();
        } catch (IllegalStateException e) {
            System.err.println("Erro de estado da interface, talvez a cena não estivesse pronta.");
        }
    }
}