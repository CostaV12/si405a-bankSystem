module com.example.javafxwithjdbc {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.javafxwithjdbc to javafx.fxml;
    exports com.example.javafxwithjdbc;
    exports com.example.javafxwithjdbc.controllers;
    opens com.example.javafxwithjdbc.controllers to javafx.fxml;
    opens com.example.javafxwithjdbc.model.entities to javafx.base;
}