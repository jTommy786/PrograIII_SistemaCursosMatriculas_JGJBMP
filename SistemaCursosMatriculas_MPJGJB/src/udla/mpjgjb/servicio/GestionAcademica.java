package udla.mpjgjb.servicio;

/**
 * Interface GestionAcademica
 * Define las operaciones basicas que deben tener todos los servicios.
 * Los servicios de Estudiante, Docente y Curso implementan esta interface.
 */
public interface GestionAcademica {
    
    /**
     * Registra un elemento en el sistema.
     */
    void registrar();
    
    /**
     * Lista todos los elementos del sistema.
     */
    void listar();
    
    /**
     * Busca un elemento por su ID.
     */
    void buscar(int id);
    
    /**
     * Actualiza la informacion de un elemento.
     */
    void actualizar(int id);
}
