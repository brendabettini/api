package org.example.telacad;

import javafx.scene.Scene;

public final class Ui {
    private Ui() {}

    public static final String CSS_PATH = "/org/example/telacad/css/login.css";

    public static void applyCss(Scene scene) {
        if (scene == null) return;
        // evita duplicar
        scene.getStylesheets().removeIf(s -> s.endsWith("css/login.css"));
        var css = Ui.class.getResource(CSS_PATH);
        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
            System.out.println("[CSS] aplicado: " + css);
        } else {
            System.err.println("[WARN] CSS não encontrado no classpath: " + CSS_PATH);
        }
    }
}
