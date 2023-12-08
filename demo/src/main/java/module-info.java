module com.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    

    opens com.example to javafx.fxml;
    exports com.example;
    exports com.example.Controller;
    opens com.example.Controller to javafx.fxml;
}
