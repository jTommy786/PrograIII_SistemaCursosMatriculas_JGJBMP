package udla.mpjgjb.modelo;

import java.time.LocalDate;

/**
 * Clase Matricula
 * Representa cuando un estudiante se inscribe en un curso.
 * Guarda el ID del estudiante, el ID del curso y la fecha de inscripcion.
 */
public class Matricula {
    private int id;
    private int idEstudiante;
    private int idCurso;
    private LocalDate fecha;

    // Constructor vacío
    public Matricula() {
    }

    // Constructor con parámetros
    public Matricula(int id, int idEstudiante, int idCurso, LocalDate fecha) {
        this.id = id;
        this.idEstudiante = idEstudiante;
        this.idCurso = idCurso;
        this.fecha = fecha;
    }

    // Constructor sin ID (para inserciones nuevas)
    public Matricula(int idEstudiante, int idCurso, LocalDate fecha) {
        this.idEstudiante = idEstudiante;
        this.idCurso = idCurso;
        this.fecha = fecha;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public int getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    /**
     * Convierte la matricula a texto para mostrarla facilmente.
     * Util para depuracion y logs.
     */
    @Override
    public String toString() {
        return "Matricula{" +
                "id=" + id +
                ", idEstudiante=" + idEstudiante +
                ", idCurso=" + idCurso +
                ", fecha=" + fecha +
                '}';
    }
}
