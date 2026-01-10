package udla.mpjgjb.servicio;

import udla.mpjgjb.dao.MatriculaDAO;
import udla.mpjgjb.dao.EstudianteDAO;
import udla.mpjgjb.dao.CursoDAO;
import udla.mpjgjb.modelo.Matricula;
import udla.mpjgjb.modelo.Estudiante;
import udla.mpjgjb.modelo.Curso;
import udla.mpjgjb.modelo.EstadoMatricula;
import udla.mpjgjb.util.Utilidades;
import udla.mpjgjb.util.SelectorPaginado;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

// Servicio para gestionar matriculas
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
    
    // Registra una nueva matricula
    @Override
    public void registrar() {
        Utilidades.imprimirTitulo("REGISTRAR NUEVA MATRICULA", 80);
        
        // Seleccionar estudiante con tabla paginada
        List<Estudiante> estudiantes = estudianteDAO.listarEstudiantes();
        if (estudiantes.isEmpty()) {
            System.out.println("ERROR: No hay estudiantes registrados en el sistema");
            return;
        }
        
        SelectorPaginado selector = new SelectorPaginado(scanner);
        int idEstudiante = selector.seleccionarEstudiante(estudiantes);

        if (idEstudiante == -1) {
            System.out.println("Operacion cancelada.");
            return;
        }
        
        // Verificar que el estudiante existe
        Estudiante estudiante = estudianteDAO.buscarEstudiantePorId(idEstudiante);
        if (estudiante == null) {
            System.out.println("ERROR: Estudiante no encontrado");
            return;
        }
        System.out.println("[OK] Estudiante: " + estudiante.getNombre());
        
        // Seleccionar curso con tabla paginada
        List<Curso> cursos = cursoDAO.listarCursos();
        if (cursos.isEmpty()) {
            System.out.println("ERROR: No hay cursos registrados en el sistema");
            return;
        }

        int idCurso = selector.seleccionarCurso(cursos);

        if (idCurso == -1) {
            System.out.println("Operacion cancelada.");
            return;
        }
        
        // Verificar que el curso existe
        Curso curso = cursoDAO.buscarCursoPorId(idCurso);
        if (curso == null) {
            System.out.println("ERROR: Curso no encontrado");
            return;
        }
        System.out.println("[OK] Curso: " + curso.getNombre());
        
        // Verificar que hay cupos disponibles
        if (curso.getCuposDisponibles() <= 0) {
            System.out.println("ERROR: No hay cupos disponibles en este curso");
            return;
        }
        System.out.println("[OK] Cupos disponibles: " + curso.getCuposDisponibles());
        
        // Verificar que no hay matrícula activa duplicada
        if (matriculaDAO.existeMatriculaActiva(idEstudiante, idCurso)) {
            System.out.println("ERROR: Este estudiante ya tiene una matricula activa en este curso");
            return;
        }
        System.out.println("[OK] No existe matricula activa previa en este curso");
        
        // Crear y registrar la matrícula
        Matricula matricula = new Matricula(idEstudiante, idCurso, LocalDate.now());
        
        if (matriculaDAO.registrarMatricula(matricula)) {
            Utilidades.imprimirLinea(80, '-');
            System.out.println("*** Matricula registrada exitosamente! ***");
            System.out.println("Estudiante: " + estudiante.getNombre());
            System.out.println("Curso:      " + curso.getNombre());
            System.out.println("Fecha:      " + LocalDate.now());
            Utilidades.imprimirLinea(80, '=');
        } else {
            System.out.println("ERROR: No se pudo registrar la matricula");
        }
    }

    // Lista todas las matriculas del sistema
    @Override
    public void listar() {
        List<Matricula> matriculas = matriculaDAO.listarMatriculas();
        
        if (matriculas.isEmpty()) {
            System.out.println("\nNo hay matriculas registradas.");
            return;
        }
        
        Utilidades.imprimirTitulo("LISTADO DE MATRICULAS", 110);
        
        // Definir anchos de columnas
        int[] anchos = {5, 8, 8, 25, 25, 12, 12};
        
        // Imprimir separador superior
        Utilidades.imprimirSeparador(anchos);
        
        // Imprimir encabezado
        String[] encabezado = {"ID", "ID_EST", "ID_CUR", "ESTUDIANTE", "CURSO", "FECHA", "ESTADO"};
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
                matricula.getFecha().toString(),
                matricula.getEstado().toString()
            };
            Utilidades.imprimirFila(fila, anchos);
        }
        
        // Imprimir separador inferior
        Utilidades.imprimirSeparador(anchos);
        System.out.println("Total de matriculas: " + matriculas.size());
    }

    // Busca una matricula por su ID
    @Override
    public void buscar(int id) {
        Matricula matricula = matriculaDAO.buscarMatriculaPorId(id);
        
        if (matricula != null) {
            Estudiante estudiante = estudianteDAO.buscarEstudiantePorId(matricula.getIdEstudiante());
            Curso curso = cursoDAO.buscarCursoPorId(matricula.getIdCurso());
            
            Utilidades.imprimirTitulo("INFORMACION DE LA MATRICULA", 80);
            System.out.println("ID Matricula: " + matricula.getId());
            System.out.println("Estudiante:   " + (estudiante != null ? estudiante.getNombre() : "No encontrado"));
            System.out.println("Curso:        " + (curso != null ? curso.getNombre() : "No encontrado"));
            System.out.println("Fecha:        " + matricula.getFecha());
            System.out.println("Estado:       " + matricula.getEstado());
            Utilidades.imprimirLinea(80, '=');
        } else {
            System.out.println("ERROR: Matricula no encontrada");
        }
    }

    // No se permite modificar matriculas directamente
    // Use editarEstadoMatricula para cambiar el estado
    @Override
    public void actualizar(int id) {
        System.out.println("ERROR: No se pueden modificar los datos de las matrículas");
        System.out.println("Use la opción 'Editar estado de matricula' para cambiar el estado.");
    }
    
    // Elimina una matricula (cambia estado a CANCELADA)
    @Override
    public void eliminar(int id) {
        // Buscar la matrícula
        Matricula matricula = matriculaDAO.buscarMatriculaPorId(id);
        if (matricula == null) {
            System.out.println("ERROR: Matricula no encontrada");
            return;
        }

        // Obtener información del estudiante y curso
        Estudiante estudiante = estudianteDAO.buscarEstudiantePorId(matricula.getIdEstudiante());
        Curso curso = cursoDAO.buscarCursoPorId(matricula.getIdCurso());

        Utilidades.imprimirTitulo("ELIMINAR MATRICULA", 80);
        System.out.println("ID Matricula: " + matricula.getId());
        System.out.println("Estudiante:   " + (estudiante != null ? estudiante.getNombre() : "No encontrado"));
        System.out.println("Curso:        " + (curso != null ? curso.getNombre() : "No encontrado"));
        System.out.println("Fecha:        " + matricula.getFecha());
        Utilidades.imprimirLinea(80, '-');

        System.out.print("\n¿Esta seguro que desea eliminar esta matricula? (S/N): ");
        String confirmacion = scanner.nextLine().trim().toUpperCase();

        if (!confirmacion.equals("S")) {
            System.out.println("Operacion cancelada.");
            return;
        }

        if (matriculaDAO.cancelarMatricula(id)) {
            System.out.println("\n*** Matricula eliminada exitosamente ***");
            System.out.println("El cupo ha sido devuelto al curso.");
        } else {
            System.out.println("ERROR: No se pudo eliminar la matricula");
        }
    }

    // Obtiene las matriculas de un estudiante especifico
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
        int[] anchos = {10, 30, 12, 12};
        
        // Imprimir separador superior
        Utilidades.imprimirSeparador(anchos);
        
        // Imprimir encabezado
        String[] encabezado = {"ID_MATRIC", "CURSO", "FECHA", "ESTADO"};
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
                matricula.getFecha().toString(),
                matricula.getEstado().toString()
            };
            Utilidades.imprimirFila(fila, anchos);
        }
        
        // Imprimir separador inferior
        Utilidades.imprimirSeparador(anchos);
        System.out.println("Total de matriculas: " + matriculas.size());
    }

    // Obtiene los estudiantes matriculados en un curso
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
        int[] anchos = {10, 25, 12, 12, 12};
        
        // Imprimir separador superior
        Utilidades.imprimirSeparador(anchos);
        
        // Imprimir encabezado
        String[] encabezado = {"ID_MATRIC", "ESTUDIANTE", "CEDULA", "FECHA", "ESTADO"};
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
                matricula.getFecha().toString(),
                matricula.getEstado().toString()
            };
            Utilidades.imprimirFila(fila, anchos);
        }
        
        // Imprimir separador inferior
        Utilidades.imprimirSeparador(anchos);
        System.out.println("Estudiantes matriculados: " + matriculas.size() + 
                         " / Cupos totales: " + curso.getCuposTotales() +
                         " / Cupos disponibles: " + curso.getCuposDisponibles());
    }
    
    // Cancela una matricula cambiando su estado a CANCELADA
    public void cancelarMatricula() {
        Utilidades.imprimirTitulo("CANCELAR MATRICULA", 80);
        System.out.print("ID de la matricula a cancelar: ");
        
        String idStr = scanner.nextLine();
        int id = Utilidades.convertirAEntero(idStr);
        
        if (id <= 0) {
            System.out.println("ERROR: ID invalido");
            return;
        }

        // Buscar la matrícula antes de cancelar
        Matricula matricula = matriculaDAO.buscarMatriculaPorId(id);
        if (matricula == null) {
            System.out.println("ERROR: Matricula no encontrada");
            return;
        }

        if (matricula.getEstado() == EstadoMatricula.CANCELADA) {
            System.out.println("ERROR: Esta matricula ya esta cancelada");
            return;
        }

        // Obtener información del estudiante y curso
        Estudiante estudiante = estudianteDAO.buscarEstudiantePorId(matricula.getIdEstudiante());
        Curso curso = cursoDAO.buscarCursoPorId(matricula.getIdCurso());

        // Mostrar información de la matrícula
        System.out.println("\nInformacion de la matricula:");
        System.out.println("  ID Matricula: " + matricula.getId());
        System.out.println("  Estudiante:   " + (estudiante != null ? estudiante.getNombre() : "No encontrado"));
        System.out.println("  Curso:        " + (curso != null ? curso.getNombre() : "No encontrado"));
        System.out.println("  Fecha:        " + matricula.getFecha());
        System.out.println("  Estado actual: " + matricula.getEstado());

        // Confirmar cancelación
        System.out.print("\n¿Esta seguro que desea cancelar esta matricula? (S/N): ");
        String confirmacion = scanner.nextLine().trim().toUpperCase();

        if (!confirmacion.equals("S")) {
            System.out.println("Operacion cancelada.");
            return;
        }

        if (matriculaDAO.cancelarMatricula(id)) {
            System.out.println("\n*** Matricula cancelada exitosamente ***");
            if (matricula.getEstado() == EstadoMatricula.ACTIVA) {
                System.out.println("El cupo ha sido devuelto al curso.");
            }
        } else {
            System.out.println("ERROR: No se pudo cancelar la matricula");
        }
    }

    // Edita el estado de una matricula
    public void editarEstadoMatricula() {
        Utilidades.imprimirTitulo("CAMBIAR ESTADO DE MATRICULA", 80);

        // Obtener lista de todas las matriculas
        List<Matricula> matriculas = matriculaDAO.listarMatriculas();
        if (matriculas.isEmpty()) {
            System.out.println("ERROR: No hay matriculas registradas en el sistema");
            return;
        }

        // Usar selector paginado para elegir la matricula
        SelectorPaginado selector = new SelectorPaginado(scanner);
        System.out.println("\nSeleccione la matricula:");
        int id = selector.seleccionarMatricula(matriculas);

        if (id == -1) {
            System.out.println("Operacion cancelada.");
            return;
        }

        // Buscar la matrícula
        Matricula matricula = matriculaDAO.buscarMatriculaPorId(id);
        if (matricula == null) {
            System.out.println("ERROR: Matricula no encontrada");
            return;
        }

        // Verificar si la matrícula está cancelada (irreversible)
        if (matricula.getEstado() == EstadoMatricula.CANCELADA) {
            System.out.println("\nERROR: Esta matricula ha sido CANCELADA");
            System.out.println("El estado CANCELADA es IRREVERSIBLE y no se puede modificar.");
            return;
        }

        // Obtener información del estudiante y curso
        Estudiante estudiante = estudianteDAO.buscarEstudiantePorId(matricula.getIdEstudiante());
        Curso curso = cursoDAO.buscarCursoPorId(matricula.getIdCurso());

        // Mostrar información de la matrícula
        System.out.println("\nInformacion de la matricula:");
        System.out.println("  ID Matricula: " + matricula.getId());
        System.out.println("  Estudiante:   " + (estudiante != null ? estudiante.getNombre() : "No encontrado"));
        System.out.println("  Curso:        " + (curso != null ? curso.getNombre() : "No encontrado"));
        System.out.println("  Fecha:        " + matricula.getFecha());
        System.out.println("  Estado actual: " + matricula.getEstado());

        // Mostrar opciones de estado
        System.out.println("\nSeleccione el nuevo estado:");
        System.out.println("1. ACTIVA");
        System.out.println("2. CANCELADA (IRREVERSIBLE)");
        System.out.println("3. TERMINADA");
        System.out.println("0. Cancelar operacion");
        System.out.print("Opcion: ");

        String opcionStr = scanner.nextLine();
        int opcion = Utilidades.convertirAEntero(opcionStr);

        if (opcion == 0) {
            System.out.println("Operacion cancelada.");
            return;
        }

        EstadoMatricula nuevoEstado;
        switch (opcion) {
            case 1:
                nuevoEstado = EstadoMatricula.ACTIVA;
                break;
            case 2:
                nuevoEstado = EstadoMatricula.CANCELADA;
                break;
            case 3:
                nuevoEstado = EstadoMatricula.TERMINADA;
                break;
            default:
                System.out.println("ERROR: Opcion invalida");
                return;
        }

        // Confirmar cambio con advertencia especial para CANCELADA
        if (nuevoEstado == EstadoMatricula.CANCELADA) {
            Utilidades.imprimirLinea(80, '!');
            System.out.println("ADVERTENCIA: Esta a punto de CANCELAR la matricula");
            System.out.println("El estado CANCELADA es IRREVERSIBLE y no podra ser modificado.");
            System.out.println("Una vez cancelada, esta matricula no podra volver a ACTIVA o TERMINADA.");
            Utilidades.imprimirLinea(80, '!');
            System.out.print("\n¿Esta COMPLETAMENTE SEGURO que desea CANCELAR esta matricula? (S/N): ");
        } else {
            System.out.print("\n¿Esta seguro que desea cambiar el estado a " + nuevoEstado + "? (S/N): ");
        }

        String confirmacion = scanner.nextLine().trim().toUpperCase();

        if (!confirmacion.equals("S")) {
            System.out.println("Operacion cancelada.");
            return;
        }

        if (matriculaDAO.cambiarEstadoMatricula(id, nuevoEstado)) {
            System.out.println("\n*** Estado de matricula actualizado exitosamente ***");
            System.out.println("Estado anterior: " + matricula.getEstado());
            System.out.println("Estado nuevo:    " + nuevoEstado);

            if (nuevoEstado == EstadoMatricula.CANCELADA) {
                System.out.println("\nNOTA: El estado CANCELADA es IRREVERSIBLE.");
            }

            if (matricula.getEstado() == EstadoMatricula.ACTIVA && nuevoEstado != EstadoMatricula.ACTIVA) {
                System.out.println("El cupo ha sido devuelto al curso.");
            } else if (matricula.getEstado() != EstadoMatricula.ACTIVA && nuevoEstado == EstadoMatricula.ACTIVA) {
                System.out.println("Se ha tomado un cupo del curso.");
            }
        } else {
            System.out.println("ERROR: No se pudo actualizar el estado de la matricula");
        }
    }
}
