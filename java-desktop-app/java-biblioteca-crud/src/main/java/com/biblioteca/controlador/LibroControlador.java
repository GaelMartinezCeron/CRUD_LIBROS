package com.biblioteca.controlador;

import com.biblioteca.dao.LibroDAO;
import com.biblioteca.modelo.Libro;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controlador principal para la interfaz de gestión de libros
 */
public class LibroControlador implements Initializable {

    // Campos del formulario
    @FXML private TextField txtNombre;
    @FXML private TextField txtAutor;
    @FXML private DatePicker dpFechaLanzamiento;
    @FXML private ComboBox<String> cmbEditorial;
    @FXML private TextField txtCosto;
    @FXML private TextField txtBuscar;

    // Tabla y columnas
    @FXML private TableView<Libro> tablaLibros;
    @FXML private TableColumn<Libro, Integer> colId;
    @FXML private TableColumn<Libro, String> colNombre;
    @FXML private TableColumn<Libro, String> colAutor;
    @FXML private TableColumn<Libro, LocalDate> colFecha;
    @FXML private TableColumn<Libro, String> colEditorial;
    @FXML private TableColumn<Libro, Double> colCosto;

    // DAO y datos
    private final LibroDAO libroDAO = new LibroDAO();
    private ObservableList<Libro> listaLibros;
    private Libro libroSeleccionado = null;

    // Lista de editoriales para el catálogo
    private final String[] EDITORIALES = {
        "Penguin Random House",
        "Editorial Planeta",
        "Santillana",
        "Alfaguara",
        "Anagrama",
        "Salamandra",
        "Tusquets Editores",
        "Seix Barral",
        "Espasa",
        "McGraw-Hill"
    };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Configurar columnas de la tabla
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaLanzamiento"));
        colEditorial.setCellValueFactory(new PropertyValueFactory<>("editorial"));
        colCosto.setCellValueFactory(new PropertyValueFactory<>("costo"));

        // Formatear columna de costo
        colCosto.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Double costo, boolean empty) {
                super.updateItem(costo, empty);
                if (empty || costo == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", costo));
                }
            }
        });

        // Cargar editoriales en el ComboBox
        cmbEditorial.setItems(FXCollections.observableArrayList(EDITORIALES));

        // Cargar datos iniciales
        cargarLibros();

        // Listener para selección en la tabla
        tablaLibros.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    libroSeleccionado = newSelection;
                    cargarDatosEnFormulario(newSelection);
                }
            }
        );
    }

    /**
     * Carga todos los libros en la tabla
     */
    private void cargarLibros() {
        listaLibros = FXCollections.observableArrayList(libroDAO.obtenerTodos());
        tablaLibros.setItems(listaLibros);
    }

    /**
     * Carga los datos del libro seleccionado en el formulario
     */
    private void cargarDatosEnFormulario(Libro libro) {
        txtNombre.setText(libro.getNombre());
        txtAutor.setText(libro.getAutor());
        dpFechaLanzamiento.setValue(libro.getFechaLanzamiento());
        cmbEditorial.setValue(libro.getEditorial());
        txtCosto.setText(String.valueOf(libro.getCosto()));
    }

    /**
     * REGISTRAR - Guarda un nuevo libro
     */
    @FXML
    private void registrar() {
        if (!validarCampos()) {
            return;
        }

        Libro nuevoLibro = new Libro(
            txtNombre.getText().trim(),
            txtAutor.getText().trim(),
            dpFechaLanzamiento.getValue(),
            cmbEditorial.getValue(),
            Double.parseDouble(txtCosto.getText().trim())
        );

        if (libroDAO.insertar(nuevoLibro)) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Libro registrado correctamente.");
            limpiarFormulario();
            cargarLibros();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo registrar el libro.");
        }
    }

    /**
     * MODIFICAR - Actualiza el libro seleccionado
     */
    @FXML
    private void modificar() {
        if (libroSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Advertencia", "Seleccione un libro de la tabla para modificar.");
            return;
        }

        if (!validarCampos()) {
            return;
        }

        // Confirmar modificación
        Optional<ButtonType> resultado = mostrarConfirmacion(
            "Confirmar Modificación",
            "¿Está seguro de que desea modificar este libro?"
        );

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            libroSeleccionado.setNombre(txtNombre.getText().trim());
            libroSeleccionado.setAutor(txtAutor.getText().trim());
            libroSeleccionado.setFechaLanzamiento(dpFechaLanzamiento.getValue());
            libroSeleccionado.setEditorial(cmbEditorial.getValue());
            libroSeleccionado.setCosto(Double.parseDouble(txtCosto.getText().trim()));

            if (libroDAO.actualizar(libroSeleccionado)) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Libro modificado correctamente.");
                limpiarFormulario();
                cargarLibros();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo modificar el libro.");
            }
        }
    }

    /**
     * ELIMINAR - Elimina el libro seleccionado
     */
    @FXML
    private void eliminar() {
        if (libroSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Advertencia", "Seleccione un libro de la tabla para eliminar.");
            return;
        }

        // Confirmar eliminación
        Optional<ButtonType> resultado = mostrarConfirmacion(
            "Confirmar Eliminación",
            "¿Está seguro de que desea eliminar el libro '" + libroSeleccionado.getNombre() + "'?"
        );

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            if (libroDAO.eliminar(libroSeleccionado.getId())) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Libro eliminado correctamente.");
                limpiarFormulario();
                cargarLibros();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo eliminar el libro.");
            }
        }
    }

    /**
     * CONSULTAR/BUSCAR - Busca libros por nombre o autor
     */
    @FXML
    private void buscar() {
        String termino = txtBuscar.getText().trim();
        
        if (termino.isEmpty()) {
            cargarLibros();
        } else {
            listaLibros = FXCollections.observableArrayList(libroDAO.buscar(termino));
            tablaLibros.setItems(listaLibros);
        }
    }

    /**
     * Limpia todos los campos del formulario
     */
    @FXML
    private void limpiarFormulario() {
        txtNombre.clear();
        txtAutor.clear();
        dpFechaLanzamiento.setValue(null);
        cmbEditorial.setValue(null);
        txtCosto.clear();
        txtBuscar.clear();
        libroSeleccionado = null;
        tablaLibros.getSelectionModel().clearSelection();
    }

    /**
     * Valida que todos los campos estén completos y correctos
     */
    private boolean validarCampos() {
        StringBuilder errores = new StringBuilder();

        if (txtNombre.getText().trim().isEmpty()) {
            errores.append("- El nombre del libro es requerido.\n");
        }
        if (txtAutor.getText().trim().isEmpty()) {
            errores.append("- El nombre del autor es requerido.\n");
        }
        if (dpFechaLanzamiento.getValue() == null) {
            errores.append("- La fecha de lanzamiento es requerida.\n");
        }
        if (cmbEditorial.getValue() == null || cmbEditorial.getValue().isEmpty()) {
            errores.append("- Debe seleccionar una editorial.\n");
        }
        if (txtCosto.getText().trim().isEmpty()) {
            errores.append("- El costo es requerido.\n");
        } else {
            try {
                double costo = Double.parseDouble(txtCosto.getText().trim());
                if (costo < 0) {
                    errores.append("- El costo debe ser un número positivo.\n");
                }
            } catch (NumberFormatException e) {
                errores.append("- El costo debe ser un número válido.\n");
            }
        }

        if (errores.length() > 0) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos Inválidos", errores.toString());
            return false;
        }

        return true;
    }

    /**
     * Muestra una alerta al usuario
     */
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Muestra un diálogo de confirmación
     */
    private Optional<ButtonType> mostrarConfirmacion(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        return alert.showAndWait();
    }
}
