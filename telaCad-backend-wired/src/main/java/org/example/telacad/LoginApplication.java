package org.example.telacad;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class LoginApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        var url = getClass().getResource("/org/example/telacad/LoginView.fxml");
        if (url == null) throw new RuntimeException("LoginView.fxml não encontrado no classpath");
        Parent root = FXMLLoader.load(url);

        Scene scene = new Scene(root);
        Ui.applyCss(scene); // aplica no boot

        // instala “gancho” global: qualquer nova Scene terá CSS automaticamente
        stage.sceneProperty().addListener((obs, oldScene, newScene) -> Ui.applyCss(newScene));

        stage.setScene(scene);
        stage.setTitle("Tela de Login");
        var bounds = Screen.getPrimary().getVisualBounds();
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
