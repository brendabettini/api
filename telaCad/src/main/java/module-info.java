module org.example.telacad {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.telacad to javafx.fxml;
    exports org.example.telacad;
}