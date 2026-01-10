package udla.mpjgjb.modelo;

/**
 * Clase Docente
 * Extiende de Persona y agrega atributos especificos
 */
public class Docente extends Persona {
    private String especialidad;

    // Constructor
    public Docente(int id, String nombre, String cedula, String especialidad) {
        super(id, nombre, cedula);
        this.especialidad = especialidad;
    }

    // Getters y Setters
    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }
}