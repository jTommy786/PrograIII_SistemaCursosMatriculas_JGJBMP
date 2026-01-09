package udla.mpjgjb.servicio;

import udla.mpjgjb.dao.DocenteDAO;
import udla.mpjgjb.modelo.Docente;
import udla.mpjgjb.util.Utilidades;

import java.util.List;
import java.util.Scanner;

/**
 * Servicio para gestionar docentes
 * Permite registrar, listar, buscar y actualizar docentes
 */
public class ServicioDocente implements GestionAcademica {
    
    private DocenteDAO docenteDAO;
    private Scanner scanner;
    
    public ServicioDocente() {
        this.docenteDAO = new DocenteDAO();
        this.scanner = new Scanner(System.in);
    }
    
    @Override
    public void registrar() {
        Utilidades.imprimirTitulo("REGISTRAR NUEVO DOCENTE", 80);
        
        System.out.print("Nombre del docente: ");
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
        
        System.out.print("Especialidad: ");
        String especialidad = scanner.nextLine();
        
        if (!Utilidades.validarTextoNoVacio(especialidad)) {
            System.out.println("ERROR: La especialidad no puede estar vacia");
            return;
        }
        
        Docente docente = new Docente(0, nombre, cedula, especialidad);
        
        if (docenteDAO.registrarDocente(docente)) {
            System.out.println("\n*** Docente registrado exitosamente ***");
        } else {
            System.out.println("ERROR: No se pudo registrar el docente");
        }
    }
    
    @Override
    public void listar() {
        List<Docente> docentes = docenteDAO.listarDocentes();
        
        if (docentes.isEmpty()) {
            System.out.println("\nNo hay docentes registrados.");
            System.out.println("\nNo hay docentes registrados.");
            return;
        }
        
        Utilidades.imprimirTitulo("LISTADO DE DOCENTES", 80);
        
        // Definir anchos de columnas
        int[] anchos = {5, 25, 12, 30};
        
        // Imprimir separador superior
        Utilidades.imprimirSeparador(anchos);
        
        // Imprimir encabezado
        String[] encabezado = {"ID", "NOMBRE", "CEDULA", "ESPECIALIDAD"};
        Utilidades.imprimirFila(encabezado, anchos);
        
        // Imprimir separador
        Utilidades.imprimirSeparador(anchos);
        
        // Imprimir cada docente
        for (Docente docente : docentes) {
            String[] fila = {
                String.valueOf(docente.getId()),
                docente.getNombre(),
                docente.getCedula(),
                docente.getEspecialidad()
            };
            Utilidades.imprimirFila(fila, anchos);
        }
        
        // Imprimir separador inferior
        Utilidades.imprimirSeparador(anchos);
        System.out.println("Total de docentes: " + docentes.size());
    }
    
    @Override
    public void buscar(int id) {
        Docente docente = docenteDAO.buscarDocentePorId(id);
        
        if (docente != null) {
            Utilidades.imprimirTitulo("INFORMACION DEL DOCENTE", 80);
            System.out.println("ID:           " + docente.getId());
            System.out.println("Nombre:       " + docente.getNombre());
            System.out.println("Cedula:       " + docente.getCedula());
            System.out.println("Especialidad: " + docente.getEspecialidad());
            Utilidades.imprimirLinea(80, '=');
            return true;
        } else {
            System.out.println("ERROR: Docente no encontrado");
        }
    }
    
    @Override
    public void actualizar(int id) {
        Docente docente = docenteDAO.buscarDocentePorId(id);
        
        if (docente == null) {
            System.out.println("ERROR: Docente no encontrado");
            return;
        }
        
        Utilidades.imprimirTitulo("ACTUALIZAR DOCENTE", 80);
        Utilidades.imprimirTitulo("ACTUALIZAR DOCENTE", 80);
        System.out.println("Docente actual: " + docente.getNombre());
        System.out.println("(Presione Enter para mantener el valor actual)");
        Utilidades.imprimirLinea(80, '-');
        System.out.println("(Presione Enter para mantener el valor actual)");
        Utilidades.imprimirLinea(80, '-');
        
        System.out.print("Nuevo nombre: ");
        String nombre = scanner.nextLine();
        if (Utilidades.validarTextoNoVacio(nombre)) {
            docente.setNombre(nombre);
        }
        
        System.out.print("Nueva cedula (10 digitos): ");
        String cedula = scanner.nextLine();
        if (Utilidades.validarTextoNoVacio(cedula)) {
            if (Utilidades.validarCedula(cedula)) {
                docente.setCedula(cedula);
            } else {
                System.out.println("ERROR: La cedula debe tener 10 digitos. No se actualizo.");
            }
        }
        
        System.out.print("Nueva especialidad: ");
        String especialidad = scanner.nextLine();
        if (Utilidades.validarTextoNoVacio(especialidad)) {
            docente.setEspecialidad(especialidad);
        }
        
        if (docenteDAO.actualizarDocente(docente)) {
            System.out.println("\n*** Docente actualizado exitosamente ***");
        } else {
            System.out.println("ERROR: No se pudo actualizar el docente");
        }
    }
}
