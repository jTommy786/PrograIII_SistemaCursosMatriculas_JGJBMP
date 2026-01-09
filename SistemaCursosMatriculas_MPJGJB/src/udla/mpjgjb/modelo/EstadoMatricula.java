package udla.mpjgjb.modelo;

// Estados posibles de una matrícula
public enum EstadoMatricula {
    ACTIVA,
    CANCELADA,
    TERMINADA;
    
    // Convierte un String a EstadoMatricula
    public static EstadoMatricula fromString(String texto) {
        if (texto == null) return ACTIVA;
        
        try {
            return EstadoMatricula.valueOf(texto.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ACTIVA;
        }
    }
}
