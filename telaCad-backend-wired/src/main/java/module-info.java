module org.example.telacad {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;

    opens org.example.telacad to javafx.fxml;
    opens org.example.telacad.models to javafx.base;
    opens org.example.telacad.db to javafx.base;
    exports org.example.telacad;
}
