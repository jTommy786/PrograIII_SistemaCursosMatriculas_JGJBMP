package udla.mpjgjb.modelo;

public class Curso {
    private int id;
    private String nombre;
    private String descripcion;
    private int cuposDisponibles;
    private int cuposTotales;
    private Integer docenteId;
    
    // Constructor vacio
    public Curso() {
    }
    
    // Constructor con parametros
    public Curso(int id, String nombre, String descripcion, int cuposTotales, Integer docenteId) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cuposTotales = cuposTotales;
        this.cuposDisponibles = cuposTotales;
        this.docenteId = docenteId;
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
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public int getCuposDisponibles() {
        return cuposDisponibles;
    }
    
    public void setCuposDisponibles(int cuposDisponibles) {
        this.cuposDisponibles = cuposDisponibles;
    }
    
    public int getCuposTotales() {
        return cuposTotales;
    }
    
    public void setCuposTotales(int cuposTotales) {
        this.cuposTotales = cuposTotales;
    }
    
    public Integer getDocenteId() {
        return docenteId;
    }
    
    public void setDocenteId(Integer docenteId) {
        this.docenteId = docenteId;
    }
    
    // Verifica si hay cupos disponibles
    public boolean hayCuposDisponibles() {
        return cuposDisponibles > 0;
    }
    
    // Reduce un cupo disponible
    public void reducirCupo() {
        if (cuposDisponibles > 0) {
            cuposDisponibles--;
        }
    }
    
    // Aumenta un cupo disponible
    public void aumentarCupo() {
        if (cuposDisponibles < cuposTotales) {
            cuposDisponibles++;
        }
    }
    
    @Override
    public String toString() {
        return "Curso{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", cuposDisponibles=" + cuposDisponibles +
                ", cuposTotales=" + cuposTotales +
                ", docenteId=" + docenteId +
                '}';
    }
}
