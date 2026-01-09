package udla.mpjgjb.servicio;

import udla.mpjgjb.dao.CursoDAO;
import udla.mpjgjb.modelo.Curso;
import udla.mpjgjb.util.Utilidades;

import java.util.List;
import java.util.Scanner;

/**
 * Servicio para gestionar cursos
 * Permite registrar, listar, buscar, actualizar y asignar docentes a cursos
 */
public class ServicioCurso implements GestionAcademica {
    
    private CursoDAO cursoDAO;
    private Scanner scanner;
    
    public ServicioCurso() {
        this.cursoDAO = new CursoDAO();
        this.scanner = new Scanner(System.in);
    }
    
    @Override
    public void registrar() {
        Utilidades.imprimirTitulo("REGISTRAR NUEVO CURSO", 80);
        
        System.out.print("Nombre del curso: ");
        String nombre = scanner.nextLine();
        
        if (!Utilidades.validarTextoNoVacio(nombre)) {
            System.out.println("ERROR: El nombre no puede estar vacio");
            return;
        }
        
        System.out.print("Descripcion: ");
        String descripcion = scanner.nextLine();
        
        System.out.print("Cupos totales: ");
        String cuposTexto = scanner.nextLine();
        int cuposTotales = Utilidades.convertirAEntero(cuposTexto);
        
        if (cuposTotales <= 0) {
            System.out.println("ERROR: Los cupos deben ser mayores a 0");
            return;
        }
        
        Curso curso = new Curso(0, nombre, descripcion, cuposTotales, null);
        
        if (cursoDAO.registrarCurso(curso)) {
            System.out.println("\n*** Curso registrado exitosamente ***");
        } else {
            System.out.println("ERROR: No se pudo registrar el curso");
        }
    }
    
    @Override
    public void listar() {
        List<Curso> cursos = cursoDAO.listarCursos();
        
        if (cursos.isEmpty()) {
            System.out.println("\nNo hay cursos registrados.");
            return;
        }
        
        Utilidades.imprimirTitulo("LISTADO DE CURSOS", 95);
        
        int[] anchos = {4, 20, 25, 8, 8, 10};
        
        Utilidades.imprimirSeparador(anchos);
        
        String[] encabezado = {"ID", "NOMBRE", "DESCRIPCION", "DISP", "TOTAL", "DOCENTE"};
        Utilidades.imprimirFila(encabezado, anchos);
        
        Utilidades.imprimirSeparador(anchos);
        
        for (Curso curso : cursos) {
            String docenteStr = (curso.getDocenteId() != null) ? 
                                 String.valueOf(curso.getDocenteId()) : "Sin asig";
            
            String[] fila = {
                String.valueOf(curso.getId()),
                curso.getNombre(),
                curso.getDescripcion(),
                String.valueOf(curso.getCuposDisponibles()),
                String.valueOf(curso.getCuposTotales()),
                docenteStr
            };
            Utilidades.imprimirFila(fila, anchos);
        }
        
        Utilidades.imprimirSeparador(anchos);
        System.out.println("Total de cursos: " + cursos.size());
    }
    
    @Override
    public void buscar(int id) {
        Curso curso = cursoDAO.buscarCursoPorId(id);
        
        if (curso != null) {
            Utilidades.imprimirTitulo("INFORMACION DEL CURSO", 80);
            System.out.println("ID:                 " + curso.getId());
            System.out.println("Nombre:             " + curso.getNombre());
            System.out.println("Descripcion:        " + curso.getDescripcion());
            System.out.println("Cupos disponibles:  " + curso.getCuposDisponibles());
            System.out.println("Cupos totales:      " + curso.getCuposTotales());
            System.out.println("Docente ID:         " + 
                             (curso.getDocenteId() != null ? curso.getDocenteId() : "Sin asignar"));
            Utilidades.imprimirLinea(80, '=');
        } else {
            System.out.println("ERROR: Curso no encontrado");
        }
    }
    
    @Override
    public void actualizar(int id) {
        Curso curso = cursoDAO.buscarCursoPorId(id);
        
        if (curso == null) {
            System.out.println("ERROR: Curso no encontrado");
            return;
        }
        
        Utilidades.imprimirTitulo("ACTUALIZAR CURSO", 80);
        System.out.println("Curso actual: " + curso.getNombre());
        System.out.println("(Presione Enter para mantener el valor actual)");
        Utilidades.imprimirLinea(80, '-');
        
        System.out.print("Nuevo nombre: ");
        String nombre = scanner.nextLine();
        if (Utilidades.validarTextoNoVacio(nombre)) {
            curso.setNombre(nombre);
        }
        
        System.out.print("Nueva descripcion: ");
        String descripcion = scanner.nextLine();
        if (Utilidades.validarTextoNoVacio(descripcion)) {
        if (Utilidades.validarTextoNoVacio(descripcion)) {
            curso.setDescripcion(descripcion);
        }
        
        System.out.print("Nuevos cupos totales: ");
        String cuposStr = scanner.nextLine();
        if (Utilidades.validarTextoNoVacio(cuposStr)) {
            int cuposTotales = Utilidades.convertirAEntero(cuposStr);
            if (cuposTotales <= 0) {
                System.out.println("ERROR: Ingrese un numero valido mayor a 0");
                return;
            }
            
            int cuposOcupados = curso.getCuposTotales() - curso.getCuposDisponibles();
            if (cuposTotales >= cuposOcupados) {
                curso.setCuposTotales(cuposTotales);
                curso.setCuposDisponibles(cuposTotales - cuposOcupados);
                curso.setCuposDisponibles(cuposTotales - cuposOcupados);
            } else {
                System.out.println("ERROR: Los cupos totales (" + cuposTotales + 
                                 ") no pueden ser menores a los ya ocupados (" + cuposOcupados + ")");
                return;
            }
        }
        
        if (cursoDAO.actualizarCurso(curso)) {
            cursoDAO.actualizarCupos(curso.getId(), curso.getCuposDisponibles());
            System.out.println("\n*** Curso actualizado exitosamente ***");
        } else {
            System.out.println("ERROR: No se pudo actualizar el curso");
        }
    }
    
    public void asignarDocente(int cursoId, int docenteId) {
        Curso curso = cursoDAO.buscarCursoPorId(cursoId);
        
        if (curso == null) {
            System.out.println("ERROR: Curso no encontrado");
            return;
        }
        
        if (cursoDAO.asignarDocente(cursoId, docenteId)) {
            System.out.println("\n*** Docente asignado exitosamente al curso ***");
        } else {
            System.out.println("ERROR: No se pudo asignar el docente");
        }
    }
    
    public void verCuposDisponibles() {
        List<Curso> cursos = cursoDAO.listarCursos();
        
        if (cursos.isEmpty()) {
            System.out.println("\nNo hay cursos registrados.");
            return;
        }
        
        Utilidades.imprimirTitulo("CUPOS DISPONIBLES POR CURSO", 80);
        
        int[] anchos = {5, 30, 12, 10, 12};
        
        Utilidades.imprimirSeparador(anchos);
        
        String[] encabezado = {"ID", "CURSO", "DISPONIBLES", "OCUPADOS", "TOTALES"};
        Utilidades.imprimirFila(encabezado, anchos);
        
        Utilidades.imprimirSeparador(anchos);
        
        for (Curso curso : cursos) {
            int ocupados = curso.getCuposTotales() - curso.getCuposDisponibles();
            
            String[] fila = {
                String.valueOf(curso.getId()),
                curso.getNombre(),
                String.valueOf(curso.getCuposDisponibles()),
                String.valueOf(ocupados),
                String.valueOf(curso.getCuposTotales())
            };
            Utilidades.imprimirFila(fila, anchos);
        }
        
        Utilidades.imprimirSeparador(anchos);
        Utilidades.imprimirTitulo("CUPOS DISPONIBLES POR CURSO", 80);
        
        int[] anchos = {5, 30, 12, 10, 12};
        
        Utilidades.imprimirSeparador(anchos);
        
        String[] encabezado = {"ID", "CURSO", "DISPONIBLES", "OCUPADOS", "TOTALES"};
        Utilidades.imprimirFila(encabezado, anchos);
        
        Utilidades.imprimirSeparador(anchos);
        
        for (Curso curso : cursos) {
            int ocupados = curso.getCuposTotales() - curso.getCuposDisponibles();
            
            String[] fila = {
                String.valueOf(curso.getId()),
                curso.getNombre(),
                String.valueOf(curso.getCuposDisponibles()),
                String.valueOf(ocupados),
                String.valueOf(curso.getCuposTotales())
            };
            Utilidades.imprimirFila(fila, anchos);
        }
        
        Utilidades.imprimirSeparador(anchos);
    }
}
