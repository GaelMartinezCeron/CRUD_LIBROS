module com.biblioteca {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.biblioteca to javafx.fxml;
    opens com.biblioteca.controlador to javafx.fxml;
    opens com.biblioteca.modelo to javafx.base;

    exports com.biblioteca;
    exports com.biblioteca.controlador;
    exports com.biblioteca.modelo;
    exports com.biblioteca.dao;
    exports com.biblioteca.database;
}
