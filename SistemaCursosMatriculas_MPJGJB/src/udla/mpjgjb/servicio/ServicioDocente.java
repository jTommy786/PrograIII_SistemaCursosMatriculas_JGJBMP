package udla.mpjgjb.servicio;

import udla.mpjgjb.dao.DocenteDAO;
import udla.mpjgjb.modelo.Docente;

import java.util.List;
import java.util.Scanner;

public class ServicioDocente implements GestionAcademica {
    
    private DocenteDAO docenteDAO;
    private Scanner scanner;
    
    public ServicioDocente() {
        this.docenteDAO = new DocenteDAO();
        this.scanner = new Scanner(System.in);
    }
    
    // Convierte un String a entero
    private int convertirAEntero(String texto) {
        if (texto == null) {
            return -1;
        }
        
        if (texto.length() == 0) {
            return -1;
        }
        
        // Verificar si todos los caracteres son digitos
        for (int i = 0; i < texto.length(); i++) {
            if (texto.toCharArray()[i] < '0' || texto.toCharArray()[i] > '9') {
                return -1;
            }
        }
        
        // Convertir a numero
        int resultado = 0;
        for (int i = 0; i < texto.length(); i++) {
            resultado = resultado * 10 + (texto.toCharArray()[i] - '0');
        }
        
        return resultado;
    }
    
    public boolean registrar() {
        System.out.println("\n=== REGISTRAR NUEVO DOCENTE ===");
        
        System.out.print("Nombre del docente: ");
        String nombre = scanner.nextLine();
        
        if (nombre.trim().isEmpty()) {
            System.out.println("ERROR: El nombre no puede estar vacio");
            return false;
        }
        
        System.out.print("Cedula: ");
        String cedula = scanner.nextLine();
        
        if (cedula.trim().isEmpty()) {
            System.out.println("ERROR: La cedula no puede estar vacia");
            return false;
        }
        
        System.out.print("Especialidad: ");
        String especialidad = scanner.nextLine();
        
        if (especialidad.trim().isEmpty()) {
            System.out.println("ERROR: La especialidad no puede estar vacia");
            return false;
        }
        
        Docente docente = new Docente(0, nombre, cedula, especialidad);
        
        if (docenteDAO.registrarDocente(docente)) {
            System.out.println("Docente registrado exitosamente");
            return true;
        } else {
            System.out.println("ERROR: No se pudo registrar el docente");
            return false;
        }
    }
    
    public void listar() {
        System.out.println("");
        System.out.println("=== LISTADO DE DOCENTES ===");
        List<Docente> docentes = docenteDAO.listarDocentes();
        
        if (docentes.isEmpty()) {
            System.out.println("No hay docentes registrados.");
            return;
        }
        
        System.out.println("====================================================");
        System.out.println("ID | NOMBRE | CEDULA | ESPECIALIDAD");
        System.out.println("====================================================");
        
        for (int i = 0; i < docentes.size(); i++) {
            Docente docente = docentes.get(i);
            System.out.println(docente.getId() + " | " + docente.getNombre() + " | " + 
                             docente.getCedula() + " | " + docente.getEspecialidad());
        }
        System.out.println("====================================================");
    }
    
    @Override
    public boolean buscar(int id) {
        Docente docente = docenteDAO.buscarDocentePorId(id);
        
        if (docente != null) {
            System.out.println("\n=== INFORMACIÓN DEL DOCENTE ===");
            System.out.println("ID: " + docente.getId());
            System.out.println("Nombre: " + docente.getNombre());
            System.out.println("Cedula: " + docente.getCedula());
            System.out.println("Especialidad: " + docente.getEspecialidad());
            return true;
        } else {
            System.out.println("ERROR: Docente no encontrado");
            return false;
        }
    }
    
    public boolean actualizar(int id) {
        Docente docente = docenteDAO.buscarDocentePorId(id);
        
        if (docente == null) {
            System.out.println("ERROR: Docente no encontrado");
            return false;
        }
        
        System.out.println("\n=== ACTUALIZAR DOCENTE ===");
        System.out.println("Docente actual: " + docente.getNombre());
        
        System.out.print("Nuevo nombre (Enter para mantener): ");
        String nombre = scanner.nextLine();
        if (!nombre.trim().isEmpty()) {
            docente.setNombre(nombre);
        }
        
        System.out.print("Nueva cedula (Enter para mantener): ");
        String cedula = scanner.nextLine();
        if (!cedula.trim().isEmpty()) {
            docente.setCedula(cedula);
        }
        
        System.out.print("Nueva especialidad (Enter para mantener): ");
        String especialidad = scanner.nextLine();
        if (!especialidad.trim().isEmpty()) {
            docente.setEspecialidad(especialidad);
        }
        
        // Nota: Necesitaría un método actualizarDocente en el DAO
        System.out.println("Docente actualizado (funcionalidad en desarrollo)");
        return true;
    }
}
