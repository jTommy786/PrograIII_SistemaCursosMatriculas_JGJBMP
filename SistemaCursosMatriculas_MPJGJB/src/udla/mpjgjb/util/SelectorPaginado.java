package udla.mpjgjb.util;

import udla.mpjgjb.modelo.Estudiante;
import udla.mpjgjb.modelo.Docente;
import udla.mpjgjb.modelo.Curso;

import java.util.List;
import java.util.Scanner;

/**
 * Clase para manejar la selección paginada de elementos
 * Muestra tablas de 5 elementos por página y permite navegar entre páginas
 */
public class SelectorPaginado {
    
    private static final int ITEMS_POR_PAGINA = 5;
    private Scanner scanner;
    
    public SelectorPaginado(Scanner scanner) {
        this.scanner = scanner;
    }
    
    /**
     * Permite seleccionar un estudiante de una lista paginada
     * @return ID del estudiante seleccionado, o -1 si se cancela
     */
    public int seleccionarEstudiante(List<Estudiante> estudiantes) {
        if (estudiantes == null || estudiantes.isEmpty()) {
            System.out.println("No hay estudiantes disponibles.");
            return -1;
        }
        
        int paginaActual = 0;
        int totalPaginas = (int) Math.ceil((double) estudiantes.size() / ITEMS_POR_PAGINA);
        
        while (true) {
            mostrarEstudiantesPaginados(estudiantes, paginaActual, totalPaginas);
            
            System.out.println("\nOpciones:");
            System.out.println("  [N] Siguiente pagina | [A] Anterior | [numero] Seleccionar ID | [0] Cancelar");
            System.out.print("Ingrese opcion: ");
            String opcion = scanner.nextLine().trim().toUpperCase();
            
            if (opcion.equals("0")) {
                return -1;
            } else if (opcion.equals("N") && paginaActual < totalPaginas - 1) {
                paginaActual++;
            } else if (opcion.equals("A") && paginaActual > 0) {
                paginaActual--;
            } else {
                // Intentar convertir a número para seleccionar ID
                int idSeleccionado = Utilidades.convertirAEntero(opcion);
                if (idSeleccionado > 0) {
                    // Verificar que el ID existe en la lista
                    for (Estudiante est : estudiantes) {
                        if (est.getId() == idSeleccionado) {
                            return idSeleccionado;
                        }
                    }
                    System.out.println("\nERROR: ID no encontrado en la lista. Intente nuevamente.");
                } else {
                    System.out.println("\nOpcion invalida.");
                }
            }
        }
    }
    
    /**
     * Permite seleccionar un docente de una lista paginada
     * @return ID del docente seleccionado, o -1 si se cancela
     */
    public int seleccionarDocente(List<Docente> docentes) {
        if (docentes == null || docentes.isEmpty()) {
            System.out.println("No hay docentes disponibles.");
            return -1;
        }
        
        int paginaActual = 0;
        int totalPaginas = (int) Math.ceil((double) docentes.size() / ITEMS_POR_PAGINA);
        
        while (true) {
            mostrarDocentesPaginados(docentes, paginaActual, totalPaginas);
            
            System.out.println("\nOpciones:");
            System.out.println("  [N] Siguiente pagina | [A] Anterior | [numero] Seleccionar ID | [0] Cancelar");
            System.out.print("Ingrese opcion: ");
            String opcion = scanner.nextLine().trim().toUpperCase();
            
            if (opcion.equals("0")) {
                return -1;
            } else if (opcion.equals("N") && paginaActual < totalPaginas - 1) {
                paginaActual++;
            } else if (opcion.equals("A") && paginaActual > 0) {
                paginaActual--;
            } else {
                int idSeleccionado = Utilidades.convertirAEntero(opcion);
                if (idSeleccionado > 0) {
                    for (Docente doc : docentes) {
                        if (doc.getId() == idSeleccionado) {
                            return idSeleccionado;
                        }
                    }
                    System.out.println("\nERROR: ID no encontrado en la lista. Intente nuevamente.");
                } else {
                    System.out.println("\nOpcion invalida.");
                }
            }
        }
    }
    
    /**
     * Permite seleccionar un curso de una lista paginada
     * @return ID del curso seleccionado, o -1 si se cancela
     */
    public int seleccionarCurso(List<Curso> cursos) {
        if (cursos == null || cursos.isEmpty()) {
            System.out.println("No hay cursos disponibles.");
            return -1;
        }
        
        int paginaActual = 0;
        int totalPaginas = (int) Math.ceil((double) cursos.size() / ITEMS_POR_PAGINA);
        
        while (true) {
            mostrarCursosPaginados(cursos, paginaActual, totalPaginas);
            
            System.out.println("\nOpciones:");
            System.out.println("  [N] Siguiente pagina | [A] Anterior | [numero] Seleccionar ID | [0] Cancelar");
            System.out.print("Ingrese opcion: ");
            String opcion = scanner.nextLine().trim().toUpperCase();
            
            if (opcion.equals("0")) {
                return -1;
            } else if (opcion.equals("N") && paginaActual < totalPaginas - 1) {
                paginaActual++;
            } else if (opcion.equals("A") && paginaActual > 0) {
                paginaActual--;
            } else {
                int idSeleccionado = Utilidades.convertirAEntero(opcion);
                if (idSeleccionado > 0) {
                    for (Curso curso : cursos) {
                        if (curso.getId() == idSeleccionado) {
                            return idSeleccionado;
                        }
                    }
                    System.out.println("\nERROR: ID no encontrado en la lista. Intente nuevamente.");
                } else {
                    System.out.println("\nOpcion invalida.");
                }
            }
        }
    }
    
    /**
     * Muestra estudiantes de forma paginada
     */
    private void mostrarEstudiantesPaginados(List<Estudiante> estudiantes, int pagina, int totalPaginas) {
        System.out.println();
        Utilidades.imprimirTitulo("SELECCIONAR ESTUDIANTE - Pagina " + (pagina + 1) + "/" + totalPaginas, 80);
        
        int inicio = pagina * ITEMS_POR_PAGINA;
        int fin = Math.min(inicio + ITEMS_POR_PAGINA, estudiantes.size());
        
        int[] anchos = {5, 30, 12, 30};
        Utilidades.imprimirSeparador(anchos);
        
        String[] encabezado = {"ID", "NOMBRE", "CEDULA", "EMAIL"};
        Utilidades.imprimirFila(encabezado, anchos);
        Utilidades.imprimirSeparador(anchos);
        
        for (int i = inicio; i < fin; i++) {
            Estudiante est = estudiantes.get(i);
            String[] fila = {
                String.valueOf(est.getId()),
                est.getNombre(),
                est.getCedula(),
                est.getEmail()
            };
            Utilidades.imprimirFila(fila, anchos);
        }
        
        Utilidades.imprimirSeparador(anchos);
        System.out.println("Mostrando " + (inicio + 1) + "-" + fin + " de " + estudiantes.size() + " estudiantes");
    }
    
    /**
     * Muestra docentes de forma paginada
     */
    private void mostrarDocentesPaginados(List<Docente> docentes, int pagina, int totalPaginas) {
        System.out.println();
        Utilidades.imprimirTitulo("SELECCIONAR DOCENTE - Pagina " + (pagina + 1) + "/" + totalPaginas, 80);
        
        int inicio = pagina * ITEMS_POR_PAGINA;
        int fin = Math.min(inicio + ITEMS_POR_PAGINA, docentes.size());
        
        int[] anchos = {5, 30, 12, 25};
        Utilidades.imprimirSeparador(anchos);
        
        String[] encabezado = {"ID", "NOMBRE", "CEDULA", "ESPECIALIDAD"};
        Utilidades.imprimirFila(encabezado, anchos);
        Utilidades.imprimirSeparador(anchos);
        
        for (int i = inicio; i < fin; i++) {
            Docente doc = docentes.get(i);
            String[] fila = {
                String.valueOf(doc.getId()),
                doc.getNombre(),
                doc.getCedula(),
                doc.getEspecialidad()
            };
            Utilidades.imprimirFila(fila, anchos);
        }
        
        Utilidades.imprimirSeparador(anchos);
        System.out.println("Mostrando " + (inicio + 1) + "-" + fin + " de " + docentes.size() + " docentes");
    }
    
    /**
     * Muestra cursos de forma paginada
     */
    private void mostrarCursosPaginados(List<Curso> cursos, int pagina, int totalPaginas) {
        System.out.println();
        Utilidades.imprimirTitulo("SELECCIONAR CURSO - Pagina " + (pagina + 1) + "/" + totalPaginas, 80);
        
        int inicio = pagina * ITEMS_POR_PAGINA;
        int fin = Math.min(inicio + ITEMS_POR_PAGINA, cursos.size());
        
        int[] anchos = {5, 30, 30, 10};
        Utilidades.imprimirSeparador(anchos);
        
        String[] encabezado = {"ID", "NOMBRE", "DESCRIPCION", "CUPOS"};
        Utilidades.imprimirFila(encabezado, anchos);
        Utilidades.imprimirSeparador(anchos);
        
        for (int i = inicio; i < fin; i++) {
            Curso curso = cursos.get(i);
            String[] fila = {
                String.valueOf(curso.getId()),
                curso.getNombre(),
                curso.getDescripcion(),
                curso.getCuposDisponibles() + "/" + curso.getCuposTotales()
            };
            Utilidades.imprimirFila(fila, anchos);
        }
        
        Utilidades.imprimirSeparador(anchos);
        System.out.println("Mostrando " + (inicio + 1) + "-" + fin + " de " + cursos.size() + " cursos");
    }
}
