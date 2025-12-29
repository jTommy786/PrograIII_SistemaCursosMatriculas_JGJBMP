package udla.mpjgjb.servicio;

import udla.mpjgjb.dao.CursoDAO;
import udla.mpjgjb.modelo.Curso;

import java.util.List;
import java.util.Scanner;

public class ServicioCurso implements GestionAcademica {
    
    private CursoDAO cursoDAO;
    private Scanner scanner;
    
    public ServicioCurso() {
        this.cursoDAO = new CursoDAO();
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
        System.out.println("\n=== REGISTRAR NUEVO CURSO ===");
        
        System.out.print("Nombre del curso: ");
        String nombre = scanner.nextLine();
        
        System.out.print("Descripcion: ");
        String descripcion = scanner.nextLine();
        
        System.out.print("Cupos totales: ");
        String cuposTexto = scanner.nextLine();
        int cuposTotales = convertirAEntero(cuposTexto);
        
        if (cuposTotales <= 0) {
            System.out.println("ERROR: Los cupos deben ser mayores a 0");
            return false;
        }
        
        Curso curso = new Curso(0, nombre, descripcion, cuposTotales, null);
        
        if (cursoDAO.registrarCurso(curso)) {
            System.out.println("Curso registrado exitosamente");
            return true;
        } else {
            System.out.println("ERROR: No se pudo registrar el curso");
            return false;
        }
    }
    
    public void listar() {
        System.out.println("");
        System.out.println("=== LISTADO DE CURSOS ===");
        List<Curso> cursos = cursoDAO.listarCursos();
        
        if (cursos.isEmpty()) {
            System.out.println("No hay cursos registrados.");
            return;
        }
        
        System.out.println("====================================================");
        System.out.println("ID | NOMBRE | DESCRIPCION | CUPOS DISP | CUPOS TOT | DOCENTE");
        System.out.println("====================================================");
        
        for (int i = 0; i < cursos.size(); i++) {
            Curso curso = cursos.get(i);
            String docenteStr = "Sin asignar";
            if (curso.getDocenteId() != null) {
                docenteStr = curso.getDocenteId() + "";
            }
            System.out.println(curso.getId() + " | " + curso.getNombre() + " | " + 
                             curso.getDescripcion() + " | " + curso.getCuposDisponibles() + 
                             " | " + curso.getCuposTotales() + " | " + docenteStr);
        }
        System.out.println("====================================================");
    }
    
    @Override
    public boolean buscar(int id) {
        Curso curso = cursoDAO.buscarCursoPorId(id);
        
        if (curso != null) {
            System.out.println("\n=== INFORMACIÓN DEL CURSO ===");
            System.out.println("ID: " + curso.getId());
            System.out.println("Nombre: " + curso.getNombre());
            System.out.println("Descripción: " + curso.getDescripcion());
            System.out.println("Cupos disponibles: " + curso.getCuposDisponibles());
            System.out.println("Cupos totales: " + curso.getCuposTotales());
            System.out.println("Docente ID: " + (curso.getDocenteId() != null ? curso.getDocenteId() : "Sin asignar"));
            return true;
        } else {
            System.out.println("ERROR: Curso no encontrado");
            return false;
        }
    }
    
    public boolean actualizar(int id) {
        Curso curso = cursoDAO.buscarCursoPorId(id);
        
        if (curso == null) {
            System.out.println("ERROR: Curso no encontrado");
            return false;
        }
        
        System.out.println("\n=== ACTUALIZAR CURSO ===");
        System.out.println("Curso actual: " + curso.getNombre());
        
        System.out.print("Nuevo nombre (Enter para mantener): ");
        String nombre = scanner.nextLine();
        if (!nombre.trim().isEmpty()) {
            curso.setNombre(nombre);
        }
        
        System.out.print("Nueva descripcion (Enter para mantener): ");
        String descripcion = scanner.nextLine();
        if (!descripcion.trim().isEmpty()) {
            curso.setDescripcion(descripcion);
        }
        
        System.out.print("Nuevos cupos totales (Enter para mantener): ");
        String cuposStr = scanner.nextLine();
        if (!cuposStr.trim().isEmpty()) {
            int cuposTotales = convertirAEntero(cuposStr);
            if (cuposTotales == -1) {
                System.out.println("ERROR: Ingrese un numero valido");
                return false;
            }
            if (cuposTotales >= (curso.getCuposTotales() - curso.getCuposDisponibles())) {
                curso.setCuposTotales(cuposTotales);
            } else {
                System.out.println("ERROR: Los cupos totales no pueden ser menores a los ya ocupados");
                return false;
            }
        }
        
        if (cursoDAO.actualizarCurso(curso)) {
            System.out.println("Curso actualizado exitosamente");
            return true;
        } else {
            System.out.println("ERROR: No se pudo actualizar el curso");
            return false;
        }
    }
    
    /**Asigna un docente a un curso*/
    public boolean asignarDocente(int cursoId, int docenteId) {
        Curso curso = cursoDAO.buscarCursoPorId(cursoId);
        
        if (curso == null) {
            System.out.println("ERROR: Curso no encontrado");
            return false;
        }
        
        if (cursoDAO.asignarDocente(cursoId, docenteId)) {
            System.out.println("Docente asignado exitosamente al curso");
            return true;
        } else {
            System.out.println("ERROR: No se pudo asignar el docente");
            return false;
        }
    }
    
    public void verCuposDisponibles() {
        System.out.println("");
        System.out.println("=== CUPOS DISPONIBLES ===");
        List<Curso> cursos = cursoDAO.listarCursos();
        
        if (cursos.isEmpty()) {
            System.out.println("No hay cursos registrados.");
            return;
        }
        
        System.out.println("====================================================");
        System.out.println("ID | CURSO | DISPONIBLES | TOTALES");
        System.out.println("====================================================");
        
        for (int i = 0; i < cursos.size(); i++) {
            Curso curso = cursos.get(i);
            String disponibilidad = "NO";
            if (curso.hayCuposDisponibles()) {
                disponibilidad = "SI";
            }
            System.out.println(curso.getId() + " | " + curso.getNombre() + " | " + 
                             disponibilidad + " " + curso.getCuposDisponibles() + 
                             " | " + curso.getCuposTotales());
        }
        System.out.println("====================================================");
    }
}
