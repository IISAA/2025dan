package pe.edu.upeu.especialidad.control;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.especialidad.Enums.TipoEspecialidad;
import pe.edu.upeu.especialidad.modelo.Especialidad;
import pe.edu.upeu.especialidad.servicio.EspecialidadServicioI;


@Controller
public class MainController {
    @Autowired
    private EspecialidadServicioI especialidadServicioI;
    int indexE = -1;
    @FXML
    TextField txtSalida;
    @FXML
    private ComboBox<TipoEspecialidad> cbxEspecialidad;
    @FXML
    private TableView<Especialidad> tableView;
    ObservableList<Especialidad> listaEspecialidades;
    @FXML
    private TableColumn<Especialidad, String> nombreColumn;
    private TableColumn<Especialidad, Void> opcColumn;


    @FXML
    public void initialize() {
        cbxEspecialidad.getItems().setAll(TipoEspecialidad.values());
        definirColumnas();
        listarEspecialidades();
    }

    public void limpiarFormulario() {
        //txtSalida.setText("");
        cbxEspecialidad.getSelectionModel().clearSelection();
    }

    @FXML
    public void registrarEspecialidad() {
        Especialidad especialidad = new Especialidad();
        especialidad.setNombre(cbxEspecialidad.getSelectionModel().getSelectedItem());
        System.out.println("Registrando especialidad...");

        if (indexE == -1) {
            especialidadServicioI.save(especialidad);
            txtSalida.setText(cbxEspecialidad.getSelectionModel().getSelectedItem().toString());
        } else {
            especialidadServicioI.update(especialidad, indexE);
            txtSalida.setText(cbxEspecialidad.getSelectionModel().getSelectedItem().toString());
            indexE = -1;
        }

        listarEspecialidades();
        limpiarFormulario();
    }

    public void definirColumnas() { //método para establecer columnas
        nombreColumn = new TableColumn("Especialidad");
        nombreColumn.setPrefWidth(200);
        opcColumn = new TableColumn<>("Opciones");
        opcColumn.setPrefWidth(200);

        tableView.getColumns().addAll(nombreColumn, opcColumn);
    }

    public void agregarAccionBotones() {
        Callback<TableColumn<Especialidad, Void>, TableCell<Especialidad, Void>> cellFactory = param -> new TableCell<>() {

            private final Button editarBtn = new Button("Editar");
            private final Button eliminarBtn = new Button("Eliminar");

            {
                editarBtn.setOnAction((event) -> {
                    Especialidad e = getTableView().getItems().get(getIndex());
                    editarDatos(e, getIndex());
                });
                eliminarBtn.setOnAction((event) -> {
                    eliminarEspecialidad(getIndex());
                });
            }

            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(editarBtn, eliminarBtn);
                    hbox.setSpacing(10);
                    setGraphic(hbox);
                }
            }
        };
        opcColumn.setCellFactory(cellFactory);
    }

    public void listarEspecialidades() {
        nombreColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre().toString()));

        agregarAccionBotones();
        listaEspecialidades = FXCollections.observableArrayList(especialidadServicioI.findAll());
        tableView.setItems(listaEspecialidades);

        for (Especialidad e : especialidadServicioI.findAll()) {
            System.out.println(e.getNombre());
        }
    }

    public void eliminarEspecialidad(int index) {
        especialidadServicioI.delete(index);
        listarEspecialidades();
    }

    public void editarDatos(Especialidad p, int index) {
        cbxEspecialidad.setValue(p.getNombre());
        txtSalida.setText(p.getNombre().toString());
        indexE = index;
    }
}
