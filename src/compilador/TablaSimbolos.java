
package compilador;

import java.util.HashMap;
import java.util.Map;

/*
  Tabla de símbolos del analizador léxico.
  
  Almacena las variables declaradas mediante Dim
  junto con su tipo de dato.
  
  Se utiliza para:
  - Verificar variables declaradas previamente
  - Validar tipos de datos
  - Comprobar si una variable es numérica
 */
public class TablaSimbolos {

    // Mapa: nombreVariable -> tipoDato
    private Map<String, String> variables;

    
     // Constructor
     
    public TablaSimbolos() {
        variables = new HashMap<>();
    }

    /**
      Registra una variable en la tabla de símbolos
     
      @param nombre nombre del identificador
      @param tipo tipo de dato (Integer, String, Boolean, Byte)
     */
    public void registrarVariable(String nombre, String tipo) {
        variables.put(nombre.toLowerCase(), tipo.toLowerCase());
    }

    /**
      Verifica si una variable ya fue declarada
     
      @param nombre nombre del identificador
      @return true si existe, false si no
     */
    public boolean existeVariable(String nombre) {
        return variables.containsKey(nombre.toLowerCase());
    }

    /**
      Obtiene el tipo de dato de una variable
     
      @param nombre nombre del identificador
      @return tipo de dato o null si no existe
     */
    public String obtenerTipo(String nombre) {
        return variables.get(nombre.toLowerCase());
    }

    /**
      Verifica si una variable es numérica
      (Integer o Byte)
     
      @param nombre nombre del identificador
      @return true si es numérica
     */
    public boolean esNumerica(String nombre) {
        String tipo = obtenerTipo(nombre);

        if (tipo == null) {
            return false;
        }

        return tipo.equals("integer") || tipo.equals("byte");
    }

    /*
      Limpia la tabla de símbolos
     */
    public void limpiar() {
        variables.clear();
    }
    
     /*
      Muestra el contenido actual de la tabla de símbolos
     */
    public void mostrarTabla() {

        if (variables.isEmpty()) {
            System.out.println("La tabla de símbolos está vacía.");
            return;
        }

        for (Map.Entry<String, String> entry : variables.entrySet()) {
            System.out.println("Variable: " + entry.getKey()
                    + " | Tipo: " + entry.getValue());
        }
    }
}
