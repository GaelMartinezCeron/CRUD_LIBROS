module com.libreria.app_libros {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.libreria.app_libros to javafx.fxml;
    exports com.libreria.app_libros;
}