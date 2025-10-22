package org.example.telacad;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Perfil_AlunoController {

    // Componentes do Formulário Principal
    @FXML private Button closeButton;
    @FXML private Button atualizarBtn;
    @FXML private VBox dialogPane;
    @FXML private TextField cursoTextField;

    // Componentes do Diálogo de Sucesso
    @FXML private Button okButton;
    @FXML private VBox sucessoDialog;

    // Componentes do Diálogo de Cursos
    @FXML private VBox cursosDialog;
    @FXML private ToggleGroup cursoToggleGroup;

    // Componente do Overlay interno
    @FXML private VBox overlayPane;

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    public void initialize() {
        setupDraggable(dialogPane);
    }

    @FXML
    private void handleAbrirCursosDialog() {
        overlayPane.setVisible(true);
        dialogPane.setDisable(true);
        cursosDialog.setVisible(true);
    }

    @FXML
    private void handleConfirmarCursos() {
        RadioButton selectedRadioButton = (RadioButton) cursoToggleGroup.getSelectedToggle();
        if (selectedRadioButton != null) {
            cursoTextField.setText(selectedRadioButton.getText());
        }
        overlayPane.setVisible(false);
        cursosDialog.setVisible(false);
        dialogPane.setDisable(false);
    }

    @FXML
    private void handleAtualizar() {
        overlayPane.setVisible(true);
        dialogPane.setVisible(false);
        cursosDialog.setVisible(false);
        sucessoDialog.setVisible(true);
    }

    /**
     * MUDANÇA: O botão "OK" agora fecha a janela de perfil inteira,
     * retornando para a Home que estava atrás.
     */
    @FXML
    private void okButton() {
        handleClose();
    }

    /**
     * Fecha apenas a janela pop-up do perfil.
     */
    @FXML
    private void handleClose() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

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