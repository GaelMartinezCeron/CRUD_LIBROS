package com.biblioteca;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Clase principal de la aplicación
 * Sistema de Gestión de Biblioteca - CRUD con JavaFX y SQLite
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Cargar la vista principal
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/biblioteca/vista/principal.fxml"));
            Parent root = loader.load();

            // Configurar la escena
            Scene scene = new Scene(root, 1100, 750);

            // Cargar estilos CSS
            scene.getStylesheets().add(getClass().getResource("/com/biblioteca/vista/estilos.css").toExternalForm());

            // Configurar el escenario principal
            primaryStage.setTitle("Sistema de Gestion de Biblioteca");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(1000);
            primaryStage.setMinHeight(700);
            primaryStage.show();

        } catch (Exception e) {
            System.err.println("Error al iniciar la aplicación: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
