package com.biblioteca.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class ConexionDB {

    // Ruta de la base de datos (se crea en la carpeta del proyecto)
    private static final String URL = "jdbc:sqlite:biblioteca.db";
    private static boolean tablaCreada = false;


    public static Connection getConexion() throws SQLException {
        Connection conexion = DriverManager.getConnection(URL);

        // Crear tabla solo la primera vez
        if (!tablaCreada) {
            crearTabla(conexion);
            tablaCreada = true;
            System.out.println("Conexión a SQLite establecida correctamente.");
        }

        return conexion;
    }


    private static void crearTabla(Connection conexion) {
        String sql = """
            CREATE TABLE IF NOT EXISTS libros (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                autor TEXT NOT NULL,
                fecha_lanzamiento TEXT NOT NULL,
                editorial TEXT NOT NULL,
                costo REAL NOT NULL
            )
        """;

        try (Statement stmt = conexion.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabla 'libros' verificada/creada correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al crear la tabla: " + e.getMessage());
        }
    }
}
