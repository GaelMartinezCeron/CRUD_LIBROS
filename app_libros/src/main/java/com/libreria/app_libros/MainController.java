package com.libreria.app_libros;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("¡Sistema CRUD de Libros iniciado correctamente!");
    }
}