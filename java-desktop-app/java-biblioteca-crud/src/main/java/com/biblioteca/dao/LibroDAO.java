package com.biblioteca.dao;

import com.biblioteca.database.ConexionDB;
import com.biblioteca.modelo.Libro;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object para operaciones CRUD de Libros
 */
public class LibroDAO {

    /**
     * CREAR - Inserta un nuevo libro en la base de datos
     */
    public boolean insertar(Libro libro) {
        String sql = "INSERT INTO libros (nombre, autor, fecha_lanzamiento, editorial, costo) VALUES (?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = ConexionDB.getConexion();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, libro.getNombre());
            pstmt.setString(2, libro.getAutor());
            pstmt.setString(3, libro.getFechaLanzamiento().toString());
            pstmt.setString(4, libro.getEditorial());
            pstmt.setDouble(5, libro.getCosto());

            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al insertar libro: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos(conn, pstmt, null);
        }
    }

    /**
     * Cierra los recursos de base de datos de forma segura
     */
    private void cerrarRecursos(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("Error al cerrar recursos: " + e.getMessage());
        }
    }

    /**
     * CONSULTAR - Obtiene todos los libros de la base de datos
     */
    public List<Libro> obtenerTodos() {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT * FROM libros ORDER BY id DESC";

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConexionDB.getConexion();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Libro libro = new Libro();
                libro.setId(rs.getInt("id"));
                libro.setNombre(rs.getString("nombre"));
                libro.setAutor(rs.getString("autor"));
                libro.setFechaLanzamiento(LocalDate.parse(rs.getString("fecha_lanzamiento")));
                libro.setEditorial(rs.getString("editorial"));
                libro.setCosto(rs.getDouble("costo"));
                libros.add(libro);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener libros: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos(conn, stmt, rs);
        }

        return libros;
    }

    /**
     * CONSULTAR - Obtiene un libro por su ID
     */
    public Libro obtenerPorId(int id) {
        String sql = "SELECT * FROM libros WHERE id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = ConexionDB.getConexion();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                Libro libro = new Libro();
                libro.setId(rs.getInt("id"));
                libro.setNombre(rs.getString("nombre"));
                libro.setAutor(rs.getString("autor"));
                libro.setFechaLanzamiento(LocalDate.parse(rs.getString("fecha_lanzamiento")));
                libro.setEditorial(rs.getString("editorial"));
                libro.setCosto(rs.getDouble("costo"));
                return libro;
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener libro: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos(conn, pstmt, rs);
        }

        return null;
    }

    /**
     * CONSULTAR - Busca libros por nombre o autor
     */
    public List<Libro> buscar(String termino) {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT * FROM libros WHERE nombre LIKE ? OR autor LIKE ? ORDER BY id DESC";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = ConexionDB.getConexion();
            pstmt = conn.prepareStatement(sql);

            String busqueda = "%" + termino + "%";
            pstmt.setString(1, busqueda);
            pstmt.setString(2, busqueda);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Libro libro = new Libro();
                libro.setId(rs.getInt("id"));
                libro.setNombre(rs.getString("nombre"));
                libro.setAutor(rs.getString("autor"));
                libro.setFechaLanzamiento(LocalDate.parse(rs.getString("fecha_lanzamiento")));
                libro.setEditorial(rs.getString("editorial"));
                libro.setCosto(rs.getDouble("costo"));
                libros.add(libro);
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar libros: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos(conn, pstmt, rs);
        }

        return libros;
    }

    /**
     * MODIFICAR - Actualiza todos los campos de un libro
     */
    public boolean actualizar(Libro libro) {
        String sql = "UPDATE libros SET nombre = ?, autor = ?, fecha_lanzamiento = ?, editorial = ?, costo = ? WHERE id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = ConexionDB.getConexion();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, libro.getNombre());
            pstmt.setString(2, libro.getAutor());
            pstmt.setString(3, libro.getFechaLanzamiento().toString());
            pstmt.setString(4, libro.getEditorial());
            pstmt.setDouble(5, libro.getCosto());
            pstmt.setInt(6, libro.getId());

            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar libro: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos(conn, pstmt, null);
        }
    }

    /**
     * ELIMINAR - Elimina un libro por su ID
     */
    public boolean eliminar(int id) {
        String sql = "DELETE FROM libros WHERE id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = ConexionDB.getConexion();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);

            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar libro: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos(conn, pstmt, null);
        }
    }
}
