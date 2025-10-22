package org.example.telacad;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Perfil_ProfessorController {

    // --- Componentes do Formulário Principal ---
    @FXML private Button closeButton;
    @FXML private Button atualizarBtn;
    @FXML private VBox dialogPane;
    @FXML private TextField cursoTextField; // Adicionado fx:id para o campo de curso

    // --- Componentes do Diálogo de Sucesso ---
    @FXML private Button okButton;
    @FXML private VBox sucessoDialog;

    // --- NOVO: Componentes do Diálogo de Cursos ---
    @FXML private VBox cursosDialog;
    @FXML private VBox cursosContainer; // O VBox que irá conter os CheckBoxes

    private double xOffset = 0;
    private double yOffset = 0;

    // NOVO: Lista de cursos que será exibida no pop-up
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

    @FXML
    public void initialize() {
        // Habilita o arraste da janela principal
        setupDraggable(dialogPane);

        // NOVO: Popula a lista de cursos com checkboxes dinamicamente
        for (String nomeCurso : TODOS_OS_CURSOS) {
            CheckBox checkBox = new CheckBox(nomeCurso);
            checkBox.getStyleClass().add("curso-checkbox"); // Adiciona uma classe de estilo
            cursosContainer.getChildren().add(checkBox);
        }
    }

    /**
     * NOVO: Ação para abrir o pop-up de seleção de cursos.
     * Chamado ao clicar no TextField de curso.
     */
    @FXML
    private void handleAbrirCursosDialog() {
        dialogPane.setDisable(true); // Desabilita o formulário principal
        cursosDialog.setVisible(true); // Mostra o pop-up de cursos
    }

    /**
     * NOVO: Ação do botão "OK" do pop-up de cursos.
     * Coleta os cursos selecionados, atualiza o campo de texto e fecha o pop-up.
     */
    @FXML
    private void handleConfirmarCursos() {
        // Filtra os checkboxes selecionados e pega o texto de cada um
        String cursosSelecionados = cursosContainer.getChildren().stream()
                .filter(node -> node instanceof CheckBox && ((CheckBox) node).isSelected())
                .map(node -> ((CheckBox) node).getText())
                .collect(Collectors.joining(", ")); // Junta os nomes com ", "

        cursoTextField.setText(cursosSelecionados); // Atualiza o campo de texto

        cursosDialog.setVisible(false); // Esconde o pop-up de cursos
        dialogPane.setDisable(false); // Reabilita o formulário principal
    }


    // --- Métodos antigos (com pequenas melhorias) ---

    @FXML
    private void handleAtualizar() {
        dialogPane.setVisible(false);
        cursosDialog.setVisible(false); // Garante que o pop-up de cursos também suma
        sucessoDialog.setVisible(true);
    }

    @FXML
    private void okButton() {
        handleClose();
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    // Método auxiliar para não repetir código de arrastar
    private void setupDraggable(VBox pane) {
        pane.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        pane.setOnMouseDragged(event -> {
            Stage stage = (Stage) pane.getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }
}