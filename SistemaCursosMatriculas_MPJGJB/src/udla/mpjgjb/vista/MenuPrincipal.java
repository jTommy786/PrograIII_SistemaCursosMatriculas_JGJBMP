package udla.mpjgjb.vista;

import java.util.Scanner;
import udla.mpjgjb.servicio.ServicioMatricula;
import udla.mpjgjb.servicio.ServicioCurso;
import udla.mpjgjb.servicio.ServicioEstudiante;
import udla.mpjgjb.servicio.ServicioDocente;
import udla.mpjgjb.util.Utilidades;

/**
 * Clase MenuPrincipal
 * Interfaz de usuario para el sistema de gestión de cursos y matrículas
 * Proporciona menús interactivos para todas las funcionalidades del sistema
 */
public class MenuPrincipal {
    
    private Scanner scanner;
    
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
                    System.out.print("ID del curso: ");
                    String inputCurso = scanner.nextLine();
                    int cursoId = Utilidades.convertirAEntero(inputCurso);
                    
                    if (cursoId == -1) {
                        System.out.println("Error: ID de curso invalido.");
                        break;
                    }
                    
                    System.out.print("ID del docente: ");
                    String inputDocente = scanner.nextLine();
                    int docenteId = Utilidades.convertirAEntero(inputDocente);
                    
                    if (docenteId == -1) {
                        System.out.println("Error: ID de docente invalido.");
                        break;
                    }
                    servicioCurso.asignarDocente(cursoId, docenteId);
                    break;
                case 4:
                    servicioCurso.verCuposDisponibles();
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
            System.out.println("5. Cancelar matricula");
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
                    System.out.print("Ingrese ID del curso: ");
                    String idCursoStr = scanner.nextLine();
                    int idCurso = Utilidades.convertirAEntero(idCursoStr);
                    if (idCurso > 0) {
                        servicioMatricula.obtenerEstudiantesDelCurso(idCurso);
                    } else {
                        System.out.println("ERROR: ID invalido");
                    }
                    break;
                case 4:
                    System.out.print("Ingrese ID del estudiante: ");
                    String idEstudianteStr = scanner.nextLine();
                    int idEstudiante = Utilidades.convertirAEntero(idEstudianteStr);
                    if (idEstudiante > 0) {
                        servicioMatricula.obtenerMatriculasDelEstudiante(idEstudiante);
                    } else {
                        System.out.println("ERROR: ID invalido");
                    }
                    break;

                case 5:
                    servicioMatricula.cancelarMatricula();
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
                    System.out.print("Ingrese ID del curso: ");
                    String idCursoStr = scanner.nextLine();
                    int idCurso = Utilidades.convertirAEntero(idCursoStr);
                    if (idCurso > 0) {
                        servicioMatricula.obtenerEstudiantesDelCurso(idCurso);
                    } else {
                        System.out.println("ERROR: ID invalido");
                    }
                    break;
                case 2:
                    System.out.print("Ingrese ID del estudiante: ");
                    String idEstudianteStr = scanner.nextLine();
                    int idEstudiante = Utilidades.convertirAEntero(idEstudianteStr);
                    if (idEstudiante > 0) {
                        servicioMatricula.obtenerMatriculasDelEstudiante(idEstudiante);
                    } else {
                        System.out.println("ERROR: ID invalido");
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
    
    // Metodo main para iniciar el sistema
    public static void main() {
        MenuPrincipal menu = new MenuPrincipal();
        menu.iniciar();
    }
}
