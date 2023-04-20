module com.example.javafxwithjdbc {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.javafxwithjdbc to javafx.fxml;
    exports com.example.javafxwithjdbc;
}