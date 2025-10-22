package org.example.telacad;

import org.example.telacad.db.UsuarioDAO;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class CadastroController {

    @FXML private VBox formContainer;
    @FXML private VBox overlayPane;
    @FXML private VBox sucessoDialog;
    @FXML private VBox cursoDialog;
    
    @FXML private TextField nomeField;
    @FXML private TextField emailField;
    @FXML private TextField senhaField;
    @FXML private TextField confirmarField;
   
    @FXML private Button cursoButton;
    @FXML private Button confirmBtn;
    
    @FXML private ToggleGroup cursoToggle;
    @FXML private RadioButton curso1;
    @FXML private RadioButton curso2;
    @FXML private RadioButton curso3;
    @FXML private RadioButton curso4;

    // Troca de tela
    private void trocarCena(ActionEvent event, String fxml, String titulo) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(titulo);
            var bounds = Screen.getPrimary().getVisualBounds();
            stage.setX(bounds.getMinX());
            stage.setY(bounds.getMinY());
            stage.setWidth(bounds.getWidth());
            stage.setHeight(bounds.getHeight());
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Confirma cadastro e já troca para tela de login
    @FXML
    void confirmarCadastro(ActionEvent event) {
        String nome = nomeField != null ? nomeField.getText() : "";
        String email = emailField != null ? emailField.getText() : "";
        String senha = senhaField != null ? senhaField.getText() : "";
        String conf = confirmarField != null ? confirmarField.getText() : "";
        String cursoEscolhido = cursoButton != null ? cursoButton.getText() : "";

        if (cursoEscolhido == null || cursoEscolhido.isBlank() || "Curso".equalsIgnoreCase(cursoEscolhido)) {
            if (cursoToggle != null && cursoToggle.getSelectedToggle() != null) {
                cursoEscolhido = ((RadioButton) cursoToggle.getSelectedToggle()).getText();
            }
        }

        if (nome.isBlank() || email.isBlank() || senha.isBlank() || conf.isBlank()) {
            System.err.println("Preencha todos os campos.");
            return;
        }
        if (!senha.equals(conf)) {
            System.err.println("Senhas não conferem.");
            return;
        }

        try {
            UsuarioDAO dao = new UsuarioDAO();
            if (dao.emailExiste(email)) {
                System.err.println("E-mail já cadastrado.");
                return;
            }
            // cadastro nasce como perfil=1 e _status=1 (regra de negócio)
            dao.cadastrarAlunoBasico(email, nome, cursoEscolhido == null ? "" : cursoEscolhido, senha);

            // Troca para tela de login após cadastro
            trocarCena(event, "/org/example/telacad/LoginView.fxml", "Login");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCursoButton() {
        if (overlayPane != null) overlayPane.setVisible(true);
        if (cursoDialog != null) cursoDialog.setVisible(true);
    }

    @FXML
    private void handleOkCursoButton() {
        if (cursoToggle != null && cursoToggle.getSelectedToggle() != null) {
            String nomeCurso = ((RadioButton) cursoToggle.getSelectedToggle()).getText();
            if (cursoButton != null) cursoButton.setText(nomeCurso);
        }
        if (overlayPane != null) overlayPane.setVisible(false);
        if (cursoDialog != null) cursoDialog.setVisible(false);
    }

    @FXML
    private void handleBackToLogin(ActionEvent event) {
        trocarCena(event, "/org/example/telacad/LoginView.fxml", "Login");
    }

    @FXML
    private void handleLoginButton(ActionEvent event) {
        trocarCena(event, "/org/example/telacad/LoginView.fxml", "Login");
    }
}