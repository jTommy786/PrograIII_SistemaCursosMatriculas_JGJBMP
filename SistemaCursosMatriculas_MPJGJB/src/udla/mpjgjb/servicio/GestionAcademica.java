package udla.mpjgjb.servicio;

/**
 * Interface GestionAcademica
 * Define las operaciones basicas para el sistema
 * Conceptos POO: Interfaces, Polimorfismo
 * @author Mateo
 */
public interface GestionAcademica {
    
    // Registra un elemento en el sistema
    boolean registrar();
    
    // Lista todos los elementos del sistema
    void listar();
    
    // Busca un elemento por su ID
    boolean buscar(int id);
    
    // Actualiza la informacion de un elemento
    boolean actualizar(int id);
}
