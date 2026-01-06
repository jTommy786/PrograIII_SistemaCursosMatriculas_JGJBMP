package udla.mpjgjb.servicio;

import udla.mpjgjb.dao.EstudianteDAO;
import udla.mpjgjb.modelo.Estudiante;

import java.util.List;
import java.util.Scanner;

public class ServicioEstudiante implements GestionAcademica {
    
    private EstudianteDAO estudianteDAO;
    private Scanner scanner;
    
    public ServicioEstudiante() {
        this.estudianteDAO = new EstudianteDAO();
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
        System.out.println("\n=== REGISTRAR NUEVO ESTUDIANTE ===");
        
        System.out.print("Nombre del estudiante: ");
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
        
        System.out.print("Email: ");
        String email = scanner.nextLine();
        
        if (email.trim().isEmpty()) {
            System.out.println("ERROR: El email no puede estar vacio");
            return false;
        }
        
        Estudiante estudiante = new Estudiante(0, nombre, cedula, email);
        
        if (estudianteDAO.registrarEstudiante(estudiante)) {
            System.out.println("Estudiante registrado exitosamente");
            return true;
        } else {
            System.out.println("ERROR: No se pudo registrar el estudiante");
            return false;
        }
    }
    
    public void listar() {
        System.out.println("");
        System.out.println("=== LISTADO DE ESTUDIANTES ===");
        List<Estudiante> estudiantes = estudianteDAO.listarEstudiantes();
        
        if (estudiantes.isEmpty()) {
            System.out.println("No hay estudiantes registrados.");
            return;
        }
        
        System.out.println("====================================================");
        System.out.println("ID | NOMBRE | CEDULA | EMAIL");
        System.out.println("====================================================");
        
        for (int i = 0; i < estudiantes.size(); i++) {
            Estudiante estudiante = estudiantes.get(i);
            System.out.println(estudiante.getId() + " | " + estudiante.getNombre() + " | " + 
                             estudiante.getCedula() + " | " + estudiante.getEmail());
        }
        System.out.println("====================================================");
    }
    
    @Override
    public boolean buscar(int id) {
        Estudiante estudiante = estudianteDAO.buscarEstudiantePorId(id);
        
        if (estudiante != null) {
            System.out.println("\n=== INFORMACIÓN DEL ESTUDIANTE ===");
            System.out.println("ID: " + estudiante.getId());
            System.out.println("Nombre: " + estudiante.getNombre());
            System.out.println("Cedula: " + estudiante.getCedula());
            System.out.println("Email: " + estudiante.getEmail());
            return true;
        } else {
            System.out.println("ERROR: Estudiante no encontrado");
            return false;
        }
    }
    
    public boolean actualizar(int id) {
        Estudiante estudiante = estudianteDAO.buscarEstudiantePorId(id);
        
        if (estudiante == null) {
            System.out.println("ERROR: Estudiante no encontrado");
            return false;
        }
        
        System.out.println("\n=== ACTUALIZAR ESTUDIANTE ===");
        System.out.println("Estudiante actual: " + estudiante.getNombre());
        
        System.out.print("Nuevo nombre (Enter para mantener): ");
        String nombre = scanner.nextLine();
        if (!nombre.trim().isEmpty()) {
            estudiante.setNombre(nombre);
        }
        
        System.out.print("Nueva cedula (Enter para mantener): ");
        String cedula = scanner.nextLine();
        if (!cedula.trim().isEmpty()) {
            estudiante.setCedula(cedula);
        }
        
        System.out.print("Nuevo email (Enter para mantener): ");
        String email = scanner.nextLine();
        if (!email.trim().isEmpty()) {
            estudiante.setEmail(email);
        }
        
        // Nota: Necesitaría un método actualizarEstudiante en el DAO
        System.out.println("Estudiante actualizado (funcionalidad en desarrollo)");
        return true;
    }
}
