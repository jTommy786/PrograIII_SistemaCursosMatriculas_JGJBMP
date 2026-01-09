package udla.mpjgjb.servicio;

// Interface para las operaciones basicas de gestion
public interface GestionAcademica {
    
    // Registra un elemento en el sistema
    void registrar();
    
    // Lista todos los elementos del sistema
    void listar();
    
    // Busca un elemento por su ID
    void buscar(int id);
    
    // Actualiza la informacion de un elemento
    void actualizar(int id);
    
    // Elimina un elemento por su ID
    void eliminar(int id);
}
