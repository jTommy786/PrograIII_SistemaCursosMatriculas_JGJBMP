package udla.mpjgjb.servicio;

import udla.mpjgjb.dao.EstudianteDAO;
import udla.mpjgjb.modelo.Estudiante;
import udla.mpjgjb.util.Utilidades;

import java.util.List;
import java.util.Scanner;

/**
 * Servicio para gestionar estudiantes
 * Permite registrar, listar, buscar y actualizar estudiantes
 */
public class ServicioEstudiante implements GestionAcademica {
    
    private EstudianteDAO estudianteDAO;
    private Scanner scanner;
    
    public ServicioEstudiante() {
        this.estudianteDAO = new EstudianteDAO();
        this.scanner = new Scanner(System.in);
    }
    
    @Override
    public void registrar() {
        Utilidades.imprimirTitulo("REGISTRAR NUEVO ESTUDIANTE", 80);
        
        System.out.print("Nombre del estudiante: ");
        String nombre = scanner.nextLine();
        
        if (!Utilidades.validarTextoNoVacio(nombre)) {
            System.out.println("ERROR: El nombre no puede estar vacio");
            return;
        }
        
        System.out.print("Cedula (10 digitos): ");
        String cedula = scanner.nextLine();
        
        if (!Utilidades.validarCedula(cedula)) {
            System.out.println("ERROR: La cedula debe tener 10 digitos");
            return;
        }
        
        System.out.print("Email: ");
        String email = scanner.nextLine();
        
        if (!Utilidades.validarEmail(email)) {
            System.out.println("ERROR: El email no tiene un formato valido");
            return;
        }
        
        Estudiante estudiante = new Estudiante(0, nombre, cedula, email);
        
        if (estudianteDAO.registrarEstudiante(estudiante)) {
            System.out.println("\n*** Estudiante registrado exitosamente ***");
        } else {
            System.out.println("ERROR: No se pudo registrar el estudiante");
        }
    }
    
    @Override
    public void listar() {
        List<Estudiante> estudiantes = estudianteDAO.listarEstudiantes();
        
        if (estudiantes.isEmpty()) {
            System.out.println("\nNo hay estudiantes registrados.");
            return;
        }
        
        Utilidades.imprimirTitulo("LISTADO DE ESTUDIANTES", 80);
        
        int[] anchos = {5, 25, 12, 30};
        
        Utilidades.imprimirSeparador(anchos);
        
        String[] encabezado = {"ID", "NOMBRE", "CEDULA", "EMAIL"};
        Utilidades.imprimirFila(encabezado, anchos);
        
        Utilidades.imprimirSeparador(anchos);
        
        for (Estudiante estudiante : estudiantes) {
            String[] fila = {
                String.valueOf(estudiante.getId()),
                estudiante.getNombre(),
                estudiante.getCedula(),
                estudiante.getEmail()
            };
            Utilidades.imprimirFila(fila, anchos);
        }
        
        Utilidades.imprimirSeparador(anchos);
        System.out.println("Total de estudiantes: " + estudiantes.size());
    }
    
    @Override
    public void buscar(int id) {
        Estudiante estudiante = estudianteDAO.buscarEstudiantePorId(id);
        
        if (estudiante != null) {
            Utilidades.imprimirTitulo("INFORMACION DEL ESTUDIANTE", 80);
            System.out.println("ID:      " + estudiante.getId());
            System.out.println("Nombre:  " + estudiante.getNombre());
            System.out.println("Cedula:  " + estudiante.getCedula());
            System.out.println("Email:   " + estudiante.getEmail());
            Utilidades.imprimirLinea(80, '=');
        } else {
            System.out.println("ERROR: Estudiante no encontrado");
        }
    }
    @Override
    public void actualizar(int id) {
        Estudiante estudiante = estudianteDAO.buscarEstudiantePorId(id);
        
        if (estudiante == null) {
            System.out.println("ERROR: Estudiante no encontrado");
            return;
        }
        
        Utilidades.imprimirTitulo("ACTUALIZAR ESTUDIANTE", 80);
        System.out.println("Estudiante actual: " + estudiante.getNombre());
        System.out.println("(Presione Enter para mantener el valor actual)");
        Utilidades.imprimirLinea(80, '-');
        
        System.out.print("Nuevo nombre: ");
        String nombre = scanner.nextLine();
        if (Utilidades.validarTextoNoVacio(nombre)) {
            estudiante.setNombre(nombre);
        }
        
        System.out.print("Nueva cedula (10 digitos): ");
        String cedula = scanner.nextLine();
        if (Utilidades.validarTextoNoVacio(cedula)) {
            if (Utilidades.validarCedula(cedula)) {
                estudiante.setCedula(cedula);
            } else {
                System.out.println("ERROR: La cedula debe tener 10 digitos. No se actualizo.");
            }
        }
        
        System.out.print("Nuevo email: ");
        String email = scanner.nextLine();
        if (Utilidades.validarTextoNoVacio(email)) {
            if (Utilidades.validarEmail(email)) {
                estudiante.setEmail(email);
            } else {
                System.out.println("ERROR: Email invalido. No se actualizo.");
            }
        }
        
        if (estudianteDAO.actualizarEstudiante(estudiante)) {
            System.out.println("\n*** Estudiante actualizado exitosamente ***");
        } else {
            System.out.println("ERROR: No se pudo actualizar el estudiante");
        }
    }
}
