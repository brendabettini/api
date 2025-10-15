package org.example.telacad;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {

        URL fxmlUrl = getClass().getResource("LoginView.fxml");
        Parent root = FXMLLoader.load(fxmlUrl);

        Scene scene = new Scene(root, 900, 650);
        

        primaryStage.setTitle("GrowU - Login");
        primaryStage.setScene(scene);

        primaryStage.setMaximized(true);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
