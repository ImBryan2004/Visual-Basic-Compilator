
package compilador;

/*
  Se validan identificadores de Visual Basic carácter por carácter.
  
  Reglas del proyecto:
  - Debe iniciar con una letra (A-Z o a-z)
  - Puede contener letras, números y guion bajo (_)
  - No puede iniciar con número
  - No puede iniciar con _
  - No puede contener espacios
 */
public class ValidadorIdentificadores {

    /**
      Valida si un identificador cumple las reglas léxicas
     
      @param identificador cadena a validar
      @return true si es válido, false si no
     */
    public boolean esIdentificadorValido(String identificador) {

        // Regla 1: no null ni vacío
        if (identificador == null || identificador.length() == 0) {
            return false;
        }

        // Regla 2: primer carácter debe ser letra
        char primerCaracter = identificador.charAt(0);

        if (!esLetra(primerCaracter)) {
            return false;
        }

        // Regla 3: recorrer el resto de caracteres
        for (int i = 1; i < identificador.length(); i++) {
            char c = identificador.charAt(i);

            if (!(esLetra(c) || esNumero(c) || c == '_')) {
                return false;
            }
        }

        return true;
    }

    
     // Verifica si un carácter es una letra
     
    private boolean esLetra(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    
     // Verifica si un carácter es un número
     
    private boolean esNumero(char c) {
        return (c >= '0' && c <= '9');
    }
}
