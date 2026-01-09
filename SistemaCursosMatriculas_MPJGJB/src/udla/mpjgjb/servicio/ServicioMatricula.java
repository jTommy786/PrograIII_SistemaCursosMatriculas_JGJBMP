package udla.mpjgjb.servicio;

import udla.mpjgjb.dao.MatriculaDAO;
import udla.mpjgjb.dao.EstudianteDAO;
import udla.mpjgjb.dao.CursoDAO;
import udla.mpjgjb.modelo.Matricula;
import udla.mpjgjb.modelo.Estudiante;
import udla.mpjgjb.modelo.Curso;
import udla.mpjgjb.util.Utilidades;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * Servicio para la gestión de matrículas
 * Valida cupos, evita duplicados y gestiona la persistencia
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
     * Registra una nueva matrícula en el sistema
     * Valida que el estudiante y curso existan, que haya cupos, y que no haya duplicados
     */
    @Override
    public boolean registrar() {
        Utilidades.imprimirTitulo("REGISTRAR NUEVA MATRICULA", 80);
        
        // Obtener ID del estudiante
        System.out.print("ID del estudiante: ");
        String idEstudianteStr = scanner.nextLine();
        int idEstudiante = Utilidades.convertirAEntero(idEstudianteStr);
        
        if (idEstudiante <= 0) {
            System.out.println("ERROR: ID de estudiante invalido");
            return false;
        }
        
        // Verificar que el estudiante existe
        Estudiante estudiante = estudianteDAO.buscarEstudiantePorId(idEstudiante);
        if (estudiante == null) {
            System.out.println("ERROR: Estudiante no encontrado");
            return false;
        }
        System.out.println("[OK] Estudiante: " + estudiante.getNombre());
        
        // Obtener ID del curso
        System.out.print("ID del curso: ");
        String idCursoStr = scanner.nextLine();
        int idCurso = Utilidades.convertirAEntero(idCursoStr);
        
        if (idCurso <= 0) {
            System.out.println("ERROR: ID de curso invalido");
            return false;
        }
        
        // Verificar que el curso existe
        Curso curso = cursoDAO.buscarCursoPorId(idCurso);
        if (curso == null) {
            System.out.println("ERROR: Curso no encontrado");
            return false;
        }
        System.out.println("[OK] Curso: " + curso.getNombre());
        
        // Verificar que hay cupos disponibles
        if (curso.getCuposDisponibles() <= 0) {
            System.out.println("ERROR: No hay cupos disponibles en este curso");
            return false;
        }
        System.out.println("[OK] Cupos disponibles: " + curso.getCuposDisponibles());
        
        // Verificar que no hay matrícula duplicada
        if (matriculaDAO.existeMatricula(idEstudiante, idCurso)) {
            System.out.println("ERROR: Este estudiante ya esta matriculado en este curso");
            return false;
        }
        System.out.println("[OK] No existe matricula previa en este curso");
        
        // Crear y registrar la matrícula
        Matricula matricula = new Matricula(idEstudiante, idCurso, LocalDate.now());
        
        if (matriculaDAO.registrarMatricula(matricula)) {
            Utilidades.imprimirLinea(80, '-');
            System.out.println("*** Matricula registrada exitosamente! ***");
            System.out.println("Estudiante: " + estudiante.getNombre());
            System.out.println("Curso:      " + curso.getNombre());
            System.out.println("Fecha:      " + LocalDate.now());
            Utilidades.imprimirLinea(80, '=');
            return true;
        } else {
            System.out.println("ERROR: No se pudo registrar la matricula");
            return false;
        }
    }
    
    /**
     * Lista todas las matrículas del sistema
     */
    @Override
    public void listar() {
        List<Matricula> matriculas = matriculaDAO.listarMatriculas();
        
        if (matriculas.isEmpty()) {
            System.out.println("\nNo hay matriculas registradas.");
            return;
        }
        
        Utilidades.imprimirTitulo("LISTADO DE MATRICULAS", 100);
        
        // Definir anchos de columnas
        int[] anchos = {5, 8, 8, 25, 25, 12};
        
        // Imprimir separador superior
        Utilidades.imprimirSeparador(anchos);
        
        // Imprimir encabezado
        String[] encabezado = {"ID", "ID_EST", "ID_CUR", "ESTUDIANTE", "CURSO", "FECHA"};
        Utilidades.imprimirFila(encabezado, anchos);
        
        // Imprimir separador
        Utilidades.imprimirSeparador(anchos);
        
        // Imprimir cada matrícula
        for (Matricula matricula : matriculas) {
            Estudiante est = estudianteDAO.buscarEstudiantePorId(matricula.getIdEstudiante());
            Curso cur = cursoDAO.buscarCursoPorId(matricula.getIdCurso());
            
            String nomEstudiante = (est != null) ? est.getNombre() : "No encontrado";
            String nomCurso = (cur != null) ? cur.getNombre() : "No encontrado";
            
            String[] fila = {
                String.valueOf(matricula.getId()),
                String.valueOf(matricula.getIdEstudiante()),
                String.valueOf(matricula.getIdCurso()),
                nomEstudiante,
                nomCurso,
                matricula.getFecha().toString()
            };
            Utilidades.imprimirFila(fila, anchos);
        }
        
        // Imprimir separador inferior
        Utilidades.imprimirSeparador(anchos);
        System.out.println("Total de matriculas: " + matriculas.size());
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
            
            Utilidades.imprimirTitulo("INFORMACION DE LA MATRICULA", 80);
            System.out.println("ID Matricula: " + matricula.getId());
            System.out.println("Estudiante:   " + (estudiante != null ? estudiante.getNombre() : "No encontrado"));
            System.out.println("Curso:        " + (curso != null ? curso.getNombre() : "No encontrado"));
            System.out.println("Fecha:        " + matricula.getFecha());
            Utilidades.imprimirLinea(80, '=');
            return true;
        } else {
            System.out.println("ERROR: Matricula no encontrada");
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
        Estudiante estudiante = estudianteDAO.buscarEstudiantePorId(idEstudiante);
        if (estudiante == null) {
            System.out.println("ERROR: Estudiante no encontrado");
            return;
        }
        
        List<Matricula> matriculas = matriculaDAO.obtenerMatriculasDelEstudiante(idEstudiante);
        
        if (matriculas.isEmpty()) {
            System.out.println("\nEl estudiante " + estudiante.getNombre() + " no tiene matriculas registradas");
            return;
        }
        
        Utilidades.imprimirTitulo("MATRICULAS DEL ESTUDIANTE", 80);
        System.out.println("Estudiante: " + estudiante.getNombre());
        Utilidades.imprimirLinea(80, '-');
        
        // Definir anchos de columnas
        int[] anchos = {10, 30, 12};
        
        // Imprimir separador superior
        Utilidades.imprimirSeparador(anchos);
        
        // Imprimir encabezado
        String[] encabezado = {"ID_MATRIC", "CURSO", "FECHA"};
        Utilidades.imprimirFila(encabezado, anchos);
        
        // Imprimir separador
        Utilidades.imprimirSeparador(anchos);
        
        // Imprimir cada matrícula
        for (Matricula matricula : matriculas) {
            Curso curso = cursoDAO.buscarCursoPorId(matricula.getIdCurso());
            String nomCurso = (curso != null) ? curso.getNombre() : "No encontrado";
            
            String[] fila = {
                String.valueOf(matricula.getId()),
                nomCurso,
                matricula.getFecha().toString()
            };
            Utilidades.imprimirFila(fila, anchos);
        }
        
        // Imprimir separador inferior
        Utilidades.imprimirSeparador(anchos);
        System.out.println("Total de matriculas: " + matriculas.size());
    }
    
    /**
     * Obtiene los estudiantes matriculados en un curso
     */
    public void obtenerEstudiantesDelCurso(int idCurso) {
        Curso curso = cursoDAO.buscarCursoPorId(idCurso);
        if (curso == null) {
            System.out.println("ERROR: Curso no encontrado");
            return;
        }
        
        List<Matricula> matriculas = matriculaDAO.obtenerMatriculasDelCurso(idCurso);
        
        if (matriculas.isEmpty()) {
            System.out.println("\nEl curso " + curso.getNombre() + " no tiene estudiantes matriculados");
            return;
        }
        
        Utilidades.imprimirTitulo("ESTUDIANTES MATRICULADOS EN CURSO", 80);
        System.out.println("Curso: " + curso.getNombre());
        Utilidades.imprimirLinea(80, '-');
        
        // Definir anchos de columnas
        int[] anchos = {10, 25, 12, 12};
        
        // Imprimir separador superior
        Utilidades.imprimirSeparador(anchos);
        
        // Imprimir encabezado
        String[] encabezado = {"ID_MATRIC", "ESTUDIANTE", "CEDULA", "FECHA"};
        Utilidades.imprimirFila(encabezado, anchos);
        
        // Imprimir separador
        Utilidades.imprimirSeparador(anchos);
        
        // Imprimir cada matrícula
        for (Matricula matricula : matriculas) {
            Estudiante estudiante = estudianteDAO.buscarEstudiantePorId(matricula.getIdEstudiante());
            String nomEstudiante = (estudiante != null) ? estudiante.getNombre() : "No encontrado";
            String cedula = (estudiante != null) ? estudiante.getCedula() : "N/A";
            
            String[] fila = {
                String.valueOf(matricula.getId()),
                nomEstudiante,
                cedula,
                matricula.getFecha().toString()
            };
            Utilidades.imprimirFila(fila, anchos);
        }
        
        // Imprimir separador inferior
        Utilidades.imprimirSeparador(anchos);
        System.out.println("Estudiantes matriculados: " + matriculas.size() + 
                         " / Cupos totales: " + curso.getCuposTotales() +
                         " / Cupos disponibles: " + curso.getCuposDisponibles());
    }
    
    /**
     * Cancela una matrícula
     */
    public boolean cancelarMatricula() {
        Utilidades.imprimirTitulo("CANCELAR MATRICULA", 80);
        System.out.print("ID de la matricula a cancelar: ");
        
        String idStr = scanner.nextLine();
        int id = Utilidades.convertirAEntero(idStr);
        
        if (id <= 0) {
            System.out.println("ERROR: ID invalido");
            return false;
        }
        
        if (matriculaDAO.cancelarMatricula(id)) {
            System.out.println("\n*** Matricula cancelada exitosamente ***");
            return true;
        } else {
            System.out.println("ERROR: No se pudo cancelar la matricula");
            return false;
        }
    }
}
