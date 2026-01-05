package udla.mpjgjb.modelo;

/**
 * Clase abstracta Persona
 * Define atributos comunes y metodos para las clases Estudiante y Docente
 */
public abstract class Persona {
    private int id;
    private String nombre;
    private String cedula;

    // Constructor
    public Persona(int id, String nombre, String cedula) {
        this.id = id;
        this.nombre = nombre;
        this.cedula = cedula;
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

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    // Metodo abstracto para obtener informacion
    public abstract String obtenerInformacion();
}