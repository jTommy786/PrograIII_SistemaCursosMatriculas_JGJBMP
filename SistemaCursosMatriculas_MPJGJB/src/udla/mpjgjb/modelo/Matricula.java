package udla.mpjgjb.modelo;

import java.time.LocalDate;

// Representa cuando un estudiante se inscribe en un curso
// Incluye el estado: ACTIVA, CANCELADA o TERMINADA
public class Matricula {
    private int id;
    private int idEstudiante;
    private int idCurso;
    private LocalDate fecha;
    private EstadoMatricula estado;

    // Constructor vacio
    public Matricula() {
        this.estado = EstadoMatricula.ACTIVA;
    }

    // Constructor con parametros
    public Matricula(int id, int idEstudiante, int idCurso, LocalDate fecha, EstadoMatricula estado) {
        this.id = id;
        this.idEstudiante = idEstudiante;
        this.idCurso = idCurso;
        this.fecha = fecha;
        this.estado = estado;
    }

    // Constructor sin ID para inserciones nuevas
    public Matricula(int idEstudiante, int idCurso, LocalDate fecha) {
        this.idEstudiante = idEstudiante;
        this.idCurso = idCurso;
        this.fecha = fecha;
        this.estado = EstadoMatricula.ACTIVA;
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

    public EstadoMatricula getEstado() {
        return estado;
    }

    public void setEstado(EstadoMatricula estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Matricula{" +
                "id=" + id +
                ", idEstudiante=" + idEstudiante +
                ", idCurso=" + idCurso +
                ", fecha=" + fecha +
                ", estado=" + estado +
                '}';
    }
}
