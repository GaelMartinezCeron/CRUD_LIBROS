package com.biblioteca.modelo;

import java.time.LocalDate;

/**
 * Modelo que representa un libro en el sistema
 */
public class Libro {
    private int id;
    private String nombre;
    private String autor;
    private LocalDate fechaLanzamiento;
    private String editorial;
    private double costo;

    // Constructor vacío
    public Libro() {}

    // Constructor completo
    public Libro(int id, String nombre, String autor, LocalDate fechaLanzamiento, String editorial, double costo) {
        this.id = id;
        this.nombre = nombre;
        this.autor = autor;
        this.fechaLanzamiento = fechaLanzamiento;
        this.editorial = editorial;
        this.costo = costo;
    }

    // Constructor sin ID (para nuevos registros)
    public Libro(String nombre, String autor, LocalDate fechaLanzamiento, String editorial, double costo) {
        this.nombre = nombre;
        this.autor = autor;
        this.fechaLanzamiento = fechaLanzamiento;
        this.editorial = editorial;
        this.costo = costo;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public LocalDate getFechaLanzamiento() {
        return fechaLanzamiento;
    }

    public void setFechaLanzamiento(LocalDate fechaLanzamiento) {
        this.fechaLanzamiento = fechaLanzamiento;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    @Override
    public String toString() {
        return "Libro{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", autor='" + autor + '\'' +
                ", fechaLanzamiento=" + fechaLanzamiento +
                ", editorial='" + editorial + '\'' +
                ", costo=" + costo +
                '}';
    }
}
