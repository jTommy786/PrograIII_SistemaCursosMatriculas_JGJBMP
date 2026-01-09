import udla.mpjgjb.vista.MenuPrincipal;

/**
 * Clase Main
 * Es el punto de inicio del programa.
 * Crea el menu principal y lo ejecuta.
 *
 * El sistema permite:
 * - Registrar estudiantes, docentes y cursos.
 * - Matricular estudiantes en cursos.
 * - Ver informacion de matriculas y cupos.
 */
public class Main {
    public static void main(String[] args) {
        // Crear e iniciar el menu principal del sistema
        MenuPrincipal menu = new MenuPrincipal();
        menu.iniciar();
    }
}