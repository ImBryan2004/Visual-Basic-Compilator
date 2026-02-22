
package compilador;

/**
 *
 * @author thebr
 */

/*
esta es la clase de errores la cual encapsula la información
de cada error encontrado durante el proceso de tokenización.
*/
public class ErrorLexico {
    
    private int numero;
    private int linea;
    private String descripcion;

    public ErrorLexico(int numero, int linea, String descripcion) {
        this.numero = numero;
        this.linea = linea;
        this.descripcion = descripcion;
    }

    public int getNumero() {
        return numero;
    }

    public int getLinea() {
        return linea;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return "Error " + numero + ". Línea " + String.format("%04d", linea) + ". " + descripcion;
    }
    
}
