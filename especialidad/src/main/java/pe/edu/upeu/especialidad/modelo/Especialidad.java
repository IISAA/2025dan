package pe.edu.upeu.especialidad.modelo;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.upeu.especialidad.Enums.TipoEspecialidad;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Especialidad {
    private IntegerProperty id;
    private TipoEspecialidad nombre;
}
