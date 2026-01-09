package udla.mpjgjb.util;

/**
 * Clase Utilidades
 * Proporciona funciones comunes para todo el sistema
 * - Conversión de String a entero de forma segura
 * - Formato de tablas bonitas con +, - y =
 * - Validaciones de entrada
 */
public class Utilidades {
    
    /**
     * Convierte un texto a numero entero.
     * Si el texto no es valido, devuelve -1.
     * Solo acepta numeros positivos.
     * Ejemplo: "123" se convierte en 123.
     */
    public static int convertirAEntero(String texto) {
        // Verificar que el texto no sea null o vacío
        if (texto == null || texto.isEmpty()) {
            return -1;
        }
        
        // Limpiar espacios
        texto = texto.trim();
        
        if (texto.isEmpty()) {
            return -1;
        }
        
        // Verificar que todos los caracteres sean dígitos
        for (int i = 0; i < texto.length(); i++) {
            if (texto.charAt(i) < '0' || texto.charAt(i) > '9') {
                return -1;
            }
        }
        
        // Convertir a número
        int resultado = 0;
        for (int i = 0; i < texto.length(); i++) {
            int digito = texto.charAt(i) - '0';
            
            // Verificar overflow (número demasiado grande)
            if (resultado > (Integer.MAX_VALUE - digito) / 10) {
                return -1;
            }
            
            resultado = resultado * 10 + digito;
        }
        
        return resultado;
    }
    
    /**
     * Imprime una linea horizontal usando un caracter.
     * Puede usar '+', '-' o '=' para hacer bordes.
     */
    public static void imprimirLinea(int ancho, char caracter) {
        for (int i = 0; i < ancho; i++) {
            System.out.print(caracter);
        }
        System.out.println();
    }
    
    /**
     * Imprime una fila de tabla con los datos en columnas.
     * columnas: array con los textos de cada columna.
     * anchos: array con el ancho de cada columna.
     */
    public static void imprimirFila(String[] columnas, int[] anchos) {
        System.out.print("| ");
        for (int i = 0; i < columnas.length; i++) {
            String celda = columnas[i];
            int ancho = anchos[i];
            
            // Ajustar texto al ancho de la columna
            if (celda.length() > ancho) {
                celda = celda.substring(0, ancho - 3) + "...";
            }
            
            System.out.print(celda);
            
            // Agregar espacios para completar el ancho
            for (int j = celda.length(); j < ancho; j++) {
                System.out.print(" ");
            }
            
            System.out.print(" | ");
        }
        System.out.println();
    }
    
    /**
     * Imprime un titulo centrado con lineas de igual.
     * titulo: el texto del titulo.
     * anchoTotal: ancho total de la tabla.
     */
    public static void imprimirTitulo(String titulo, int anchoTotal) {
        System.out.println();
        imprimirLinea(anchoTotal, '=');
        
        // Centrar el título
        int espaciosAntes = (anchoTotal - titulo.length()) / 2;
        for (int i = 0; i < espaciosAntes; i++) {
            System.out.print(" ");
        }
        System.out.println(titulo);
        
        imprimirLinea(anchoTotal, '=');
    }
    
    /**
     * Imprime una linea separadora de tabla con + y -.
     * anchos: array con el ancho de cada columna.
     */
    public static void imprimirSeparador(int[] anchos) {
        System.out.print("+");
        for (int ancho : anchos) {
            for (int i = 0; i < ancho + 2; i++) {
                System.out.print("-");
            }
            System.out.print("+");
        }
        System.out.println();
    }
    
    /**
     * Verifica que un texto no este vacio.
     * Devuelve true si tiene contenido, false si esta vacio.
     */
    public static boolean validarTextoNoVacio(String texto) {
        return texto != null && !texto.trim().isEmpty();
    }
    
    /**
     * Valida que el email tenga formato basico.
     * Verifica que tenga @ y punto despues del @.
     */
    public static boolean validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        // Verificar que tenga @
        if (!email.contains("@")) {
            return false;
        }
        
        // Verificar que haya algo antes y despues del @
        int posicionArroba = email.indexOf("@");
        if (posicionArroba == 0 || posicionArroba == email.length() - 1) {
            return false;
        }
        
        // Verificar que haya punto despues del @
        String despuesArroba = email.substring(posicionArroba);
        if (!despuesArroba.contains(".")) {
            return false;
        }
        
        // Verificar que el punto no sea inmediatamente despues del @
        if (email.indexOf(".", posicionArroba) == posicionArroba + 1) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Valida que una cedula tenga exactamente 10 digitos numericos.
     */
    public static boolean validarCedula(String cedula) {
        if (cedula == null || cedula.length() != 10) {
            return false;
        }
        
        // Verificar que todos los caracteres sean digitos
        for (int i = 0; i < cedula.length(); i++) {
            if (cedula.charAt(i) < '0' || cedula.charAt(i) > '9') {
                return false;
            }
        }
        
        return true;
    }
}
