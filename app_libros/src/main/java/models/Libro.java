package com.libreria.app_libros.models;

import java.time.LocalDate;

public class Libro {
    private int id;
    private String nombreLibro;
    private String nombreAutor;
    private LocalDate fechaLanzamiento;
    private String editorial;
    private double costo;

    // Constructor vacío
    public Libro() {}

    // Constructor con parámetros
    public Libro(String nombreLibro, String nombreAutor, LocalDate fechaLanzamiento,
                 String editorial, double costo) {
        this.nombreLibro = nombreLibro;
        this.nombreAutor = nombreAutor;
        this.fechaLanzamiento = fechaLanzamiento;
        this.editorial = editorial;
        this.costo = costo;
    }

    // Constructor completo
    public Libro(int id, String nombreLibro, String nombreAutor, LocalDate fechaLanzamiento,
                 String editorial, double costo) {
        this.id = id;
        this.nombreLibro = nombreLibro;
        this.nombreAutor = nombreAutor;
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

    public String getNombreLibro() {
        return nombreLibro;
    }

    public void setNombreLibro(String nombreLibro) {
        this.nombreLibro = nombreLibro;
    }

    public String getNombreAutor() {
        return nombreAutor;
    }

    public void setNombreAutor(String nombreAutor) {
        this.nombreAutor = nombreAutor;
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
        return nombreLibro + " - " + nombreAutor + " (" + editorial + ")";
    }
}