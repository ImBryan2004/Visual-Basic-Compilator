package compilador;

import java.util.ArrayList;
import java.util.List;
/*
  Analizador Léxico.
  
  Recorre el código fuente línea por línea y aplica
  los validadores léxicos correspondientes.
 */
public class AnalizadorLexico {

    private List<String> lineas;
    private List<ErrorLexico> errores;
    private TablaSimbolos tablaSimbolos;

    private ValidadorModule validadorModule;
    private ValidadorEndModule validadorEndModule;
    private ValidadorDim validadorDim;
    private ValidadorWriteLine validadorWriteLine;
    private ValidadorIdentificadores validadorIdentificadores;
    private ValidadorComentarios validadorComentarios;
    private ValidadorAsignaciones validadorAsignaciones;

    /**
      Constructor del analizador léxico
     
      @param lineas líneas del archivo .vb
     */
    public AnalizadorLexico(List<String> lineas) {
        this.lineas = lineas;
        this.errores = new ArrayList<>();
        this.tablaSimbolos = new TablaSimbolos();

        this.validadorModule = new ValidadorModule();
        this.validadorEndModule = new ValidadorEndModule(validadorModule);
        this.validadorDim = new ValidadorDim(tablaSimbolos, validadorModule);
        this.validadorWriteLine = new ValidadorWriteLine();
        this.validadorIdentificadores = new ValidadorIdentificadores();
        this.validadorComentarios = new ValidadorComentarios();
        this.validadorAsignaciones = new ValidadorAsignaciones(tablaSimbolos);
    }

    /**
      Ejecuta el análisis léxico completo
     
      @return lista de errores encontrados
     */
    public List<ErrorLexico> analizar() {

        for (int i = 0; i < lineas.size(); i++) {
            String linea = lineas.get(i);
            int numeroLinea = i + 1;
            
            boolean esUltimaLinea = (i == lineas.size() - 1);
            
             // PASO 1: Validar comentarios
             
            errores.addAll(
                    validadorComentarios.validar(linea, numeroLinea)
            );

            // Si es comentario válido, se ignora la lÃ­nea
            if (validadorComentarios.esComentarioValido(linea)) {
                continue;
            }

            
             // PASO 2: Validar estructura Module
             
            errores.addAll(
                    validadorModule.validar(linea, numeroLinea)
            );

            
             // PASO 3: Validar declaraciones Dim
             
            errores.addAll(
                    validadorDim.validar(linea, numeroLinea)
            );

            
             // PASO 4: Validar Console.WriteLine
             
            errores.addAll(
                    validadorWriteLine.validar(linea, numeroLinea)
            );
            
            // PASO 5: Validar asingaciones
            errores.addAll(
                    validadorAsignaciones.validar(linea, numeroLinea)
            );
            
            // End Module
            errores.addAll(
                    validadorEndModule.validar(linea, numeroLinea, esUltimaLinea)
            );
        }


        return errores;
    }
    
    public TablaSimbolos getTablaSimbolos() {
    return tablaSimbolos;
    }
}
