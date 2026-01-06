package udla.mpjgjb.servicio;

import udla.mpjgjb.dao.MatriculaDAO;
import udla.mpjgjb.dao.EstudianteDAO;
import udla.mpjgjb.dao.CursoDAO;
import udla.mpjgjb.modelo.Matricula;
import udla.mpjgjb.modelo.Estudiante;
import udla.mpjgjb.modelo.Curso;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * Servicio para la gestión de matrículas
 * Valida cupos, evita duplicados y gestiona la persistencia
 * Conceptos POO: Polimorfismo, Encapsulamiento
 * @author Julián
 */
public class ServicioMatricula implements GestionAcademica {
    
    private MatriculaDAO matriculaDAO;
    private EstudianteDAO estudianteDAO;
    private CursoDAO cursoDAO;
    private Scanner scanner;
    
    public ServicioMatricula() {
        this.matriculaDAO = new MatriculaDAO();
        this.estudianteDAO = new EstudianteDAO();
        this.cursoDAO = new CursoDAO();
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Convierte un String a entero de forma segura
     */
    private int convertirAEntero(String texto) {
        if (texto == null || texto.length() == 0) {
            return -1;
        }
        
        for (int i = 0; i < texto.length(); i++) {
            if (texto.charAt(i) < '0' || texto.charAt(i) > '9') {
                return -1;
            }
        }
        
        int resultado = 0;
        for (int i = 0; i < texto.length(); i++) {
            resultado = resultado * 10 + (texto.charAt(i) - '0');
        }
        
        return resultado;
    }
    
    /**
     * Registra una nueva matrícula en el sistema
     * Valida que el estudiante y curso existan, que haya cupos, y que no haya duplicados
     */
    @Override
    public boolean registrar() {
        System.out.println("\n=== REGISTRAR NUEVA MATRÍCULA ===");
        
        // Obtener ID del estudiante
        System.out.print("ID del estudiante: ");
        String idEstudianteStr = scanner.nextLine();
        int idEstudiante = convertirAEntero(idEstudianteStr);
        
        if (idEstudiante <= 0) {
            System.out.println("ERROR: ID de estudiante inválido");
            return false;
        }
        
        // Verificar que el estudiante existe
        Estudiante estudiante = estudianteDAO.buscarEstudiantePorId(idEstudiante);
        if (estudiante == null) {
            System.out.println("ERROR: Estudiante no encontrado");
            return false;
        }
        System.out.println("✓ Estudiante encontrado: " + estudiante.getNombre());
        
        // Obtener ID del curso
        System.out.print("ID del curso: ");
        String idCursoStr = scanner.nextLine();
        int idCurso = convertirAEntero(idCursoStr);
        
        if (idCurso <= 0) {
            System.out.println("ERROR: ID de curso inválido");
            return false;
        }
        
        // Verificar que el curso existe
        Curso curso = cursoDAO.buscarCursoPorId(idCurso);
        if (curso == null) {
            System.out.println("ERROR: Curso no encontrado");
            return false;
        }
        System.out.println("✓ Curso encontrado: " + curso.getNombre());
        
        // Verificar que hay cupos disponibles
        if (curso.getCuposDisponibles() <= 0) {
            System.out.println("ERROR: No hay cupos disponibles en este curso");
            return false;
        }
        System.out.println("✓ Cupos disponibles: " + curso.getCuposDisponibles());
        
        // Verificar que no hay matrícula duplicada
        if (matriculaDAO.existeMatricula(idEstudiante, idCurso)) {
            System.out.println("ERROR: Este estudiante ya está matriculado en este curso");
            return false;
        }
        System.out.println("✓ No existe matrícula previa en este curso");
        
        // Crear y registrar la matrícula
        Matricula matricula = new Matricula(idEstudiante, idCurso, LocalDate.now());
        
        if (matriculaDAO.registrarMatricula(matricula)) {
            System.out.println("\n✓ ¡Matrícula registrada exitosamente!");
            System.out.println("  Estudiante: " + estudiante.getNombre());
            System.out.println("  Curso: " + curso.getNombre());
            System.out.println("  Fecha: " + LocalDate.now());
            return true;
        } else {
            System.out.println("ERROR: No se pudo registrar la matrícula");
            return false;
        }
    }
    
    /**
     * Lista todas las matrículas del sistema
     */
    @Override
    public void listar() {
        System.out.println("\n=== LISTADO DE MATRÍCULAS ===");
        List<Matricula> matriculas = matriculaDAO.listarMatriculas();
        
        if (matriculas.isEmpty()) {
            System.out.println("No hay matrículas registradas.");
            return;
        }
        
        System.out.println("====================================================");
        System.out.println("ID | ID_EST | ID_CURSO | ESTUDIANTE | CURSO | FECHA");
        System.out.println("====================================================");
        
        for (Matricula matricula : matriculas) {
            Estudiante est = estudianteDAO.buscarEstudiantePorId(matricula.getIdEstudiante());
            Curso cur = cursoDAO.buscarCursoPorId(matricula.getIdCurso());
            
            String nomEstudiante = (est != null) ? est.getNombre() : "No encontrado";
            String nomCurso = (cur != null) ? cur.getNombre() : "No encontrado";
            
            System.out.println(matricula.getId() + " | " + 
                             matricula.getIdEstudiante() + " | " + 
                             matricula.getIdCurso() + " | " + 
                             nomEstudiante + " | " + 
                             nomCurso + " | " + 
                             matricula.getFecha());
        }
        System.out.println("====================================================");
        System.out.println("Total de matrículas: " + matriculas.size());
    }
    
    /**
     * Busca una matrícula por su ID
     */
    @Override
    public boolean buscar(int id) {
        Matricula matricula = matriculaDAO.buscarMatriculaPorId(id);
        
        if (matricula != null) {
            Estudiante estudiante = estudianteDAO.buscarEstudiantePorId(matricula.getIdEstudiante());
            Curso curso = cursoDAO.buscarCursoPorId(matricula.getIdCurso());
            
            System.out.println("\n=== INFORMACIÓN DE LA MATRÍCULA ===");
            System.out.println("ID Matrícula: " + matricula.getId());
            System.out.println("Estudiante: " + (estudiante != null ? estudiante.getNombre() : "No encontrado"));
            System.out.println("Curso: " + (curso != null ? curso.getNombre() : "No encontrado"));
            System.out.println("Fecha: " + matricula.getFecha());
            return true;
        } else {
            System.out.println("ERROR: Matrícula no encontrada");
            return false;
        }
    }
    
    /**
     * Actualiza una matrícula (placeholder)
     */
    @Override
    public boolean actualizar(int id) {
        System.out.println("ERROR: No se pueden actualizar matrículas");
        return false;
    }
    
    /**
     * Obtiene las matrículas de un estudiante específico
     */
    public void obtenerMatriculasDelEstudiante(int idEstudiante) {
        System.out.println("\n=== MATRÍCULAS DEL ESTUDIANTE ===");
        
        Estudiante estudiante = estudianteDAO.buscarEstudiantePorId(idEstudiante);
        if (estudiante == null) {
            System.out.println("ERROR: Estudiante no encontrado");
            return;
        }
        
        List<Matricula> matriculas = matriculaDAO.obtenerMatriculasDelEstudiante(idEstudiante);
        
        if (matriculas.isEmpty()) {
            System.out.println("El estudiante " + estudiante.getNombre() + " no tiene matrículas registradas");
            return;
        }
        
        System.out.println("Estudiante: " + estudiante.getNombre());
        System.out.println("====================================================");
        System.out.println("ID | CURSO | FECHA");
        System.out.println("====================================================");
        
        for (Matricula matricula : matriculas) {
            Curso curso = cursoDAO.buscarCursoPorId(matricula.getIdCurso());
            String nomCurso = (curso != null) ? curso.getNombre() : "No encontrado";
            
            System.out.println(matricula.getId() + " | " + nomCurso + " | " + matricula.getFecha());
        }
        System.out.println("====================================================");
        System.out.println("Total de matrículas: " + matriculas.size());
    }
    
    /**
     * Obtiene los estudiantes matriculados en un curso
     */
    public void obtenerEstudiantesDelCurso(int idCurso) {
        System.out.println("\n=== ESTUDIANTES MATRICULADOS EN CURSO ===");
        
        Curso curso = cursoDAO.buscarCursoPorId(idCurso);
        if (curso == null) {
            System.out.println("ERROR: Curso no encontrado");
            return;
        }
        
        List<Matricula> matriculas = matriculaDAO.obtenerMatriculasDelCurso(idCurso);
        
        if (matriculas.isEmpty()) {
            System.out.println("El curso " + curso.getNombre() + " no tiene estudiantes matriculados");
            return;
        }
        
        System.out.println("Curso: " + curso.getNombre());
        System.out.println("====================================================");
        System.out.println("ID | ESTUDIANTE | CÉDULA | FECHA");
        System.out.println("====================================================");
        
        for (Matricula matricula : matriculas) {
            Estudiante estudiante = estudianteDAO.buscarEstudiantePorId(matricula.getIdEstudiante());
            String nomEstudiante = (estudiante != null) ? estudiante.getNombre() : "No encontrado";
            String cedula = (estudiante != null) ? estudiante.getCedula() : "No encontrada";
            
            System.out.println(matricula.getId() + " | " + nomEstudiante + " | " + cedula + " | " + matricula.getFecha());
        }
        System.out.println("====================================================");
        System.out.println("Total de estudiantes: " + matriculas.size() + 
                         " / Cupos totales: " + curso.getCuposTotales());
    }
    
    /**
     * Cancela una matrícula
     */
    public boolean cancelarMatricula() {
        System.out.println("\n=== CANCELAR MATRÍCULA ===");
        System.out.print("ID de la matrícula a cancelar: ");
        
        String idStr = scanner.nextLine();
        int id = convertirAEntero(idStr);
        
        if (id <= 0) {
            System.out.println("ERROR: ID inválido");
            return false;
        }
        
        if (matriculaDAO.cancelarMatricula(id)) {
            System.out.println("✓ Matrícula cancelada exitosamente");
            return true;
        } else {
            System.out.println("ERROR: No se pudo cancelar la matrícula");
            return false;
        }
    }
}
