package udla.mpjgjb.vista;

import java.util.Scanner;
import java.util.List;
import udla.mpjgjb.servicio.ServicioMatricula;
import udla.mpjgjb.servicio.ServicioCurso;
import udla.mpjgjb.servicio.ServicioEstudiante;
import udla.mpjgjb.servicio.ServicioDocente;
import udla.mpjgjb.util.Utilidades;
import udla.mpjgjb.util.SelectorPaginado;
import udla.mpjgjb.dao.CursoDAO;
import udla.mpjgjb.dao.DocenteDAO;
import udla.mpjgjb.dao.EstudianteDAO;
import udla.mpjgjb.modelo.Curso;
import udla.mpjgjb.modelo.Docente;
import udla.mpjgjb.modelo.Estudiante;

/**
 * Clase MenuPrincipal
 * Interfaz de usuario para el sistema de gestión de cursos y matrículas
 * Proporciona menús interactivos para todas las funcionalidades del sistema
 */
public class MenuPrincipal {
    
    private final Scanner scanner;
    
    public MenuPrincipal() {
        this.scanner = new Scanner(System.in);
    }
    
    public void iniciar() {
        boolean salir = false;
        
        while (!salir) {
            mostrarMenuPrincipal();
            
            String entrada = scanner.nextLine();
            
            if (entrada.isEmpty()) {
                System.out.println("\nError: Debe ingresar una opcion.");
                continue;
            }
            
            int opcion = Utilidades.convertirAEntero(entrada);
            
            if (opcion == -1) {
                System.out.println("\nError: Ingrese un numero valido.");
                continue;
            }
            
            switch (opcion) {
                case 1:
                    menuGestion();
                    break;
                case 2:
                    menuMatriculas();
                    break;
                case 3:
                    menuConsultas();
                    break;
                case 0:
                    salir = true;
                    System.out.println("\nGracias por usar el sistema. Hasta pronto!");
                    break;
                default:
                    System.out.println("\nOpcion invalida. Intente nuevamente.");
            }
        }
    }
    
    // Muestra el menu principal
    private void mostrarMenuPrincipal() {
        System.out.println();
        System.out.println("====================================================");
        System.out.println("   SISTEMA DE GESTION DE CURSOS Y MATRICULAS");
        System.out.println("   Unidad Educativa San Francisco de la Alvernia");
        System.out.println("====================================================");
        System.out.println("1. Gestion");
        System.out.println("2. Matriculas");
        System.out.println("3. Consultas");
        System.out.println("0. Salir");
        System.out.println("====================================================");
        System.out.print("Seleccione una opcion: ");
    }
    
    private void menuGestion() {
        boolean volver = false;
        
        while (!volver) {
            System.out.println();
            System.out.println("====================================================");
            System.out.println("                  --- Gestion ---");
            System.out.println("====================================================");
            System.out.println("1. Estudiantes");
            System.out.println("2. Docentes");
            System.out.println("3. Cursos");
            System.out.println("0. Volver al menu principal");
            System.out.println("====================================================");
            System.out.print("Seleccione una opcion: ");
            
            String entrada = scanner.nextLine();
            int opcion = Utilidades.convertirAEntero(entrada);
            
            if (opcion == -1) {
                System.out.println("\nError: Ingrese un numero valido.");
                continue;
            }
            
            switch (opcion) {
                case 1:
                    menuEstudiantes();
                    break;
                case 2:
                    menuDocentes();
                    break;
                case 3:
                    menuCursos();
                    break;
                case 0:
                    volver = true;
                    break;
                default:
                    System.out.println("\nOpcion invalida.");
            }
        }
    }
    
    private void menuEstudiantes() {
        ServicioEstudiante servicioEstudiante = new ServicioEstudiante();
        EstudianteDAO estudianteDAO = new EstudianteDAO();
        boolean volver = false;
        
        while (!volver) {
            System.out.println();
            System.out.println("====================================================");
            System.out.println("         --- Gestion de Estudiantes ---");
            System.out.println("====================================================");
            System.out.println("1. Registrar estudiante");
            System.out.println("2. Listar estudiantes");
            System.out.println("3. Buscar estudiante");
            System.out.println("4. Actualizar estudiante");
            System.out.println("5. Eliminar estudiante");
            System.out.println("0. Volver al menu principal");
            System.out.println("====================================================");
            System.out.print("Seleccione una opcion: ");
            
            String entrada = scanner.nextLine();
            int opcion = Utilidades.convertirAEntero(entrada);
            
            if (opcion == -1) {
                System.out.println("\nError: Ingrese un numero valido.");
                continue;
            }
            
            switch (opcion) {
                case 1:
                    servicioEstudiante.registrar();
                    break;
                case 2:
                    servicioEstudiante.listar();
                    break;
                case 3:
                    System.out.print("Ingrese ID del estudiante: ");
                    String idEstudianteStr = scanner.nextLine();
                    int idEstudiante = Utilidades.convertirAEntero(idEstudianteStr);
                    if (idEstudiante > 0) {
                        servicioEstudiante.buscar(idEstudiante);
                    } else {
                        System.out.println("ERROR: ID invalido");
                    }
                    break;
                case 4:
                    System.out.print("Ingrese ID del estudiante a actualizar: ");
                    String idActStr = scanner.nextLine();
                    int idAct = Utilidades.convertirAEntero(idActStr);
                    if (idAct > 0) {
                        servicioEstudiante.actualizar(idAct);
                    } else {
                        System.out.println("ERROR: ID invalido");
                    }
                    break;
                case 5:
                    List<Estudiante> estudiantesElim = estudianteDAO.listarEstudiantes();
                    if (estudiantesElim.isEmpty()) {
                        System.out.println("ERROR: No hay estudiantes registrados");
                        break;
                    }

                    SelectorPaginado selectorElimEst = new SelectorPaginado(scanner);
                    System.out.println("\nSeleccione el estudiante a eliminar:");
                    int idElim = selectorElimEst.seleccionarEstudiante(estudiantesElim);

                    if (idElim > 0) {
                        servicioEstudiante.eliminar(idElim);
                    } else {
                        System.out.println("Operacion cancelada");
                    }
                    break;
                case 0:
                    volver = true;
                    break;
                default:
                    System.out.println("\nOpcion invalida.");
            }
        }
    }
    
    // Menu de gestion de docentes
    private void menuDocentes() {
        ServicioDocente servicioDocente = new ServicioDocente();
        DocenteDAO docenteDAO = new DocenteDAO();
        boolean volver = false;
        
        while (!volver) {
            System.out.println();
            System.out.println("====================================================");
            System.out.println("           --- Gestion de Docentes ---");
            System.out.println("====================================================");
            System.out.println("1. Registrar docente");
            System.out.println("2. Listar docentes");
            System.out.println("3. Buscar docente");
            System.out.println("4. Actualizar docente");
            System.out.println("5. Eliminar docente");
            System.out.println("0. Volver al menu principal");
            System.out.println("====================================================");
            System.out.print("Seleccione una opcion: ");
            
            String entrada = scanner.nextLine();
            int opcion = Utilidades.convertirAEntero(entrada);
            
            if (opcion == -1) {
                System.out.println("\nError: Ingrese un numero valido.");
                continue;
            }
            
            switch (opcion) {
                case 1:
                    servicioDocente.registrar();
                    break;
                case 2:
                    servicioDocente.listar();
                    break;
                case 3:
                    System.out.print("Ingrese ID del docente: ");
                    String idDocenteStr = scanner.nextLine();
                    int idDocente = Utilidades.convertirAEntero(idDocenteStr);
                    if (idDocente > 0) {
                        servicioDocente.buscar(idDocente);
                    } else {
                        System.out.println("ERROR: ID invalido");
                    }
                    break;
                case 4:
                    System.out.print("Ingrese ID del docente a actualizar: ");
                    String idActStr = scanner.nextLine();
                    int idAct = Utilidades.convertirAEntero(idActStr);
                    if (idAct > 0) {
                        servicioDocente.actualizar(idAct);
                    } else {
                        System.out.println("ERROR: ID invalido");
                    }
                    break;
                case 5:
                    List<Docente> docentesElim = docenteDAO.listarDocentes();
                    if (docentesElim.isEmpty()) {
                        System.out.println("ERROR: No hay docentes registrados");
                        break;
                    }

                    SelectorPaginado selectorElimDoc = new SelectorPaginado(scanner);
                    System.out.println("\nSeleccione el docente a eliminar:");
                    int idElimDoc = selectorElimDoc.seleccionarDocente(docentesElim);

                    if (idElimDoc > 0) {
                        servicioDocente.eliminar(idElimDoc);
                    } else {
                        System.out.println("Operacion cancelada");
                    }
                    break;
                case 0:
                    volver = true;
                    break;
                default:
                    System.out.println("\nOpcion invalida.");
            }
        }
    }
    
    private void menuCursos() {
        ServicioCurso servicioCurso = new ServicioCurso();
        CursoDAO cursoDAO = new CursoDAO();
        DocenteDAO docenteDAO = new DocenteDAO();
        boolean volver = false;
        
        while (!volver) {
            System.out.println();
            System.out.println("====================================================");
            System.out.println("            --- Gestion de Cursos ---");
            System.out.println("====================================================");
            System.out.println("1. Crear curso");
            System.out.println("2. Listar cursos");
            System.out.println("3. Asignar docente a curso");
            System.out.println("4. Ver cupos disponibles");
            System.out.println("5. Actualizar curso");
            System.out.println("6. Eliminar curso");
            System.out.println("0. Volver al menu principal");
            System.out.println("====================================================");
            System.out.print("Seleccione una opcion: ");
            
            String entrada = scanner.nextLine();
            int opcion = Utilidades.convertirAEntero(entrada);
            
            if (opcion == -1) {
                System.out.println("\nError: Ingrese un numero valido.");
                continue;
            }
            
            switch (opcion) {
                case 1:
                    servicioCurso.registrar();
                    break;
                case 2:
                    servicioCurso.listar();
                    break;
                case 3:
                    // Usar selector paginado para asignar docente
                    List<Curso> cursos = cursoDAO.listarCursos();
                    if (cursos.isEmpty()) {
                        System.out.println("ERROR: No hay cursos registrados");
                        break;
                    }

                    SelectorPaginado selector = new SelectorPaginado(scanner);
                    System.out.println("\nSeleccione el curso:");
                    int cursoId = selector.seleccionarCurso(cursos);
                    
                    if (cursoId == -1) {
                        System.out.println("Operacion cancelada.");
                        break;
                    }

                    List<Docente> docentes = docenteDAO.listarDocentes();
                    if (docentes.isEmpty()) {
                        System.out.println("ERROR: No hay docentes registrados");
                        break;
                    }
                    
                    System.out.println("\nSeleccione el docente:");
                    int docenteId = selector.seleccionarDocente(docentes);
                    
                    if (docenteId == -1) {
                        System.out.println("Operacion cancelada.");
                        break;
                    }

                    servicioCurso.asignarDocente(cursoId, docenteId);
                    break;
                case 4:
                    servicioCurso.verCuposDisponibles();
                    break;
                case 5:
                    System.out.print("Ingrese ID del curso a actualizar: ");
                    String idActCursoStr = scanner.nextLine();
                    int idActCurso = Utilidades.convertirAEntero(idActCursoStr);
                    if (idActCurso > 0) {
                        servicioCurso.actualizar(idActCurso);
                    } else {
                        System.out.println("ERROR: ID invalido");
                    }
                    break;
                case 6:
                    List<Curso> cursosElim = cursoDAO.listarCursos();
                    if (cursosElim.isEmpty()) {
                        System.out.println("ERROR: No hay cursos registrados");
                        break;
                    }

                    SelectorPaginado selectorElimCurso = new SelectorPaginado(scanner);
                    System.out.println("\nSeleccione el curso a eliminar:");
                    int idElimCurso = selectorElimCurso.seleccionarCurso(cursosElim);

                    if (idElimCurso > 0) {
                        servicioCurso.eliminar(idElimCurso);
                    } else {
                        System.out.println("Operacion cancelada");
                    }
                    break;
                case 0:
                    volver = true;
                    break;
                default:
                    System.out.println("\nOpcion invalida.");
            }
        }
    }
    
    private void menuMatriculas() {
        ServicioMatricula servicioMatricula = new ServicioMatricula();
        CursoDAO cursoDAO = new CursoDAO();
        EstudianteDAO estudianteDAO = new EstudianteDAO();
        boolean volver = false;
        
        while (!volver) {
            System.out.println();
            System.out.println("====================================================");
            System.out.println("              --- Matriculas ---");
            System.out.println("====================================================");
            System.out.println("1. Matricular estudiante");
            System.out.println("2. Listar matriculas");
            System.out.println("3. Ver estudiantes matriculados en curso");
            System.out.println("4. Ver cursos de un estudiante");
            System.out.println("5. Cambiar estado de matricula");
            System.out.println("0. Volver al menu principal");
            System.out.println("====================================================");
            System.out.print("Seleccione una opcion: ");
            
            String entrada = scanner.nextLine();
            int opcion = Utilidades.convertirAEntero(entrada);
            
            if (opcion == -1) {
                System.out.println("\nError: Ingrese un numero valido.");
                continue;
            }
            
            switch (opcion) {
                case 1:
                    servicioMatricula.registrar();
                    break;
                case 2:
                    servicioMatricula.listar();
                    break;
                case 3:
                    List<Curso> cursos = cursoDAO.listarCursos();
                    if (cursos.isEmpty()) {
                        System.out.println("ERROR: No hay cursos registrados");
                        break;
                    }

                    SelectorPaginado selector3 = new SelectorPaginado(scanner);
                    System.out.println("\nSeleccione el curso:");
                    int idCurso = selector3.seleccionarCurso(cursos);

                    if (idCurso > 0) {
                        servicioMatricula.obtenerEstudiantesDelCurso(idCurso);
                    } else {
                        System.out.println("Operacion cancelada");
                    }
                    break;
                case 4:
                    List<Estudiante> estudiantes = estudianteDAO.listarEstudiantes();
                    if (estudiantes.isEmpty()) {
                        System.out.println("ERROR: No hay estudiantes registrados");
                        break;
                    }

                    SelectorPaginado selector4 = new SelectorPaginado(scanner);
                    System.out.println("\nSeleccione el estudiante:");
                    int idEstudiante = selector4.seleccionarEstudiante(estudiantes);

                    if (idEstudiante > 0) {
                        servicioMatricula.obtenerMatriculasDelEstudiante(idEstudiante);
                    } else {
                        System.out.println("Operacion cancelada");
                    }
                    break;

                case 5:
                    servicioMatricula.editarEstadoMatricula();
                    break;
                case 0:
                    volver = true;
                    break;
                default:
                    System.out.println("\nOpcion invalida.");
            }
        }
    }
    
    // Menu de consultas
    private void menuConsultas() {
        ServicioMatricula servicioMatricula = new ServicioMatricula();
        CursoDAO cursoDAO = new CursoDAO();
        EstudianteDAO estudianteDAO = new EstudianteDAO();
        boolean volver = false;
        
        while (!volver) {
            System.out.println();
            System.out.println("====================================================");
            System.out.println("               --- Consultas ---");
            System.out.println("====================================================");
            System.out.println("1. Ver estudiantes por curso");
            System.out.println("2. Ver cursos por estudiante");
            System.out.println("3. Buscar matricula");
            System.out.println("0. Volver al menu principal");
            System.out.println("====================================================");
            System.out.print("Seleccione una opcion: ");
            
            String entrada = scanner.nextLine();
            int opcion = Utilidades.convertirAEntero(entrada);
            
            if (opcion == -1) {
                System.out.println("\nError: Ingrese un numero valido.");
                continue;
            }
            
            switch (opcion) {
                case 1:
                    List<Curso> cursos = cursoDAO.listarCursos();
                    if (cursos.isEmpty()) {
                        System.out.println("ERROR: No hay cursos registrados");
                        break;
                    }

                    SelectorPaginado selector1 = new SelectorPaginado(scanner);
                    System.out.println("\nSeleccione el curso:");
                    int idCurso = selector1.seleccionarCurso(cursos);

                    if (idCurso > 0) {
                        servicioMatricula.obtenerEstudiantesDelCurso(idCurso);
                    } else {
                        System.out.println("Operacion cancelada");
                    }
                    break;
                case 2:
                    List<Estudiante> estudiantes = estudianteDAO.listarEstudiantes();
                    if (estudiantes.isEmpty()) {
                        System.out.println("ERROR: No hay estudiantes registrados");
                        break;
                    }

                    SelectorPaginado selector2 = new SelectorPaginado(scanner);
                    System.out.println("\nSeleccione el estudiante:");
                    int idEstudiante = selector2.seleccionarEstudiante(estudiantes);

                    if (idEstudiante > 0) {
                        servicioMatricula.obtenerMatriculasDelEstudiante(idEstudiante);
                    } else {
                        System.out.println("Operacion cancelada");
                    }
                    break;
                case 3:
                    System.out.print("Ingrese ID de la matricula: ");
                    String idMatriculaStr = scanner.nextLine();
                    int idMatricula = Utilidades.convertirAEntero(idMatriculaStr);
                    if (idMatricula > 0) {
                        servicioMatricula.buscar(idMatricula);
                    } else {
                        System.out.println("ERROR: ID invalido");
                    }
                    break;
                case 0:
                    volver = true;
                    break;
                default:
                    System.out.println("\nOpcion invalida.");
            }
        }
    }
}
