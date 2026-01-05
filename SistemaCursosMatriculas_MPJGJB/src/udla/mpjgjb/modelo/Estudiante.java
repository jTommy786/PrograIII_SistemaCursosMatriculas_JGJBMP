package udla.mpjgjb.modelo;

/**
 * Clase Estudiante
 * Extiende de Persona y agrega atributos especificos
 */
public class Estudiante extends Persona {
    private String email;

    // Constructor
    public Estudiante(int id, String nombre, String cedula, String email) {
        super(id, nombre, cedula);
        this.email = email;
    }

    // Getters y Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String obtenerInformacion() {
        return "Estudiante: " + getNombre() + ", Cedula: " + getCedula() + ", Email: " + email;
    }
}