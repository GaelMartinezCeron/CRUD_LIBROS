package com.libreria.app_libros.models;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static final String URL = "jdbc:sqlite:libros.db";

    // Conectar a la base de datos
    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
            System.out.println("✅ Conexión a SQLite establecida.");
        } catch (SQLException e) {
            System.out.println("❌ Error al conectar: " + e.getMessage());
        }
        return conn;
    }

    // Crear tablas si no existen
    public static void crearTablas() {
        String sqlLibros = """
            CREATE TABLE IF NOT EXISTS libros (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre_libro TEXT NOT NULL,
                nombre_autor TEXT NOT NULL,
                fecha_lanzamiento DATE NOT NULL,
                editorial TEXT NOT NULL,
                costo REAL NOT NULL
            )""";

        String sqlEditoriales = """
            CREATE TABLE IF NOT EXISTS editoriales (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL UNIQUE
            )""";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            // Crear tablas
            stmt.execute(sqlLibros);
            stmt.execute(sqlEditoriales);
            System.out.println("✅ Tablas verificadas/creadas.");

            // Insertar editoriales por defecto
            String[] editorialesDefault = {
                    "Penguin Random House", "HarperCollins", "Simon & Schuster",
                    "Macmillan", "Hachette Livre", "Editorial Planeta",
                    "Grupo Santillana", "Fondo de Cultura Económica"
            };

            for (String editorial : editorialesDefault) {
                String insert = "INSERT OR IGNORE INTO editoriales (nombre) VALUES (?)";
                try (PreparedStatement pstmt = conn.prepareStatement(insert)) {
                    pstmt.setString(1, editorial);
                    pstmt.executeUpdate();
                }
            }
            System.out.println("✅ Editoriales por defecto insertadas.");

        } catch (SQLException e) {
            System.out.println("❌ Error al crear tablas: " + e.getMessage());
        }
    }

    // CRUD: Create (Insertar libro)
    public static void insertarLibro(Libro libro) {
        String sql = "INSERT INTO libros(nombre_libro, nombre_autor, fecha_lanzamiento, editorial, costo) VALUES(?,?,?,?,?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, libro.getNombreLibro());
            pstmt.setString(2, libro.getNombreAutor());
            pstmt.setDate(3, Date.valueOf(libro.getFechaLanzamiento()));
            pstmt.setString(4, libro.getEditorial());
            pstmt.setDouble(5, libro.getCosto());
            pstmt.executeUpdate();
            System.out.println("✅ Libro insertado: " + libro.getNombreLibro());
        } catch (SQLException e) {
            System.out.println("❌ Error al insertar: " + e.getMessage());
        }
    }

    // CRUD: Read (Obtener todos los libros)
    public static List<Libro> obtenerTodosLibros() {
        String sql = "SELECT * FROM libros ORDER BY nombre_libro";
        List<Libro> libros = new ArrayList<>();

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Libro libro = new Libro();
                libro.setId(rs.getInt("id"));
                libro.setNombreLibro(rs.getString("nombre_libro"));
                libro.setNombreAutor(rs.getString("nombre_autor"));
                libro.setFechaLanzamiento(rs.getDate("fecha_lanzamiento").toLocalDate());
                libro.setEditorial(rs.getString("editorial"));
                libro.setCosto(rs.getDouble("costo"));
                libros.add(libro);
            }
        } catch (SQLException e) {
            System.out.println("❌ Error al obtener libros: " + e.getMessage());
        }
        return libros;
    }

    // Obtener editoriales para ComboBox
    public static List<String> obtenerEditoriales() {
        String sql = "SELECT nombre FROM editoriales ORDER BY nombre";
        List<String> editoriales = new ArrayList<>();

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                editoriales.add(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            System.out.println("❌ Error al obtener editoriales: " + e.getMessage());
        }
        return editoriales;
    }

    // CRUD: Update (Actualizar libro)
    public static void actualizarLibro(Libro libro) {
        String sql = "UPDATE libros SET nombre_libro = ?, nombre_autor = ?, "
                + "fecha_lanzamiento = ?, editorial = ?, costo = ? WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, libro.getNombreLibro());
            pstmt.setString(2, libro.getNombreAutor());
            pstmt.setDate(3, Date.valueOf(libro.getFechaLanzamiento()));
            pstmt.setString(4, libro.getEditorial());
            pstmt.setDouble(5, libro.getCosto());
            pstmt.setInt(6, libro.getId());
            pstmt.executeUpdate();
            System.out.println("✅ Libro actualizado: " + libro.getNombreLibro());
        } catch (SQLException e) {
            System.out.println("❌ Error al actualizar: " + e.getMessage());
        }
    }

    // CRUD: Delete (Eliminar libro)
    public static void eliminarLibro(int id) {
        String sql = "DELETE FROM libros WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("✅ Libro eliminado ID: " + id);
        } catch (SQLException e) {
            System.out.println("❌ Error al eliminar: " + e.getMessage());
        }
    }

    // Buscar libros por nombre o autor
    public static List<Libro> buscarLibros(String criterio) {
        String sql = "SELECT * FROM libros WHERE nombre_libro LIKE ? OR nombre_autor LIKE ? ORDER BY nombre_libro";
        List<Libro> libros = new ArrayList<>();

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + criterio + "%");
            pstmt.setString(2, "%" + criterio + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Libro libro = new Libro();
                libro.setId(rs.getInt("id"));
                libro.setNombreLibro(rs.getString("nombre_libro"));
                libro.setNombreAutor(rs.getString("nombre_autor"));
                libro.setFechaLanzamiento(rs.getDate("fecha_lanzamiento").toLocalDate());
                libro.setEditorial(rs.getString("editorial"));
                libro.setCosto(rs.getDouble("costo"));
                libros.add(libro);
            }
        } catch (SQLException e) {
            System.out.println("❌ Error al buscar: " + e.getMessage());
        }
        return libros;
    }
}