
package compilador;

import java.util.ArrayList;
import java.util.List;

public class ValidadorEndModule {

    private ValidadorModule validadorModule;
    private boolean endModuleEncontrado = false;

    /*
      Constructor
      Recibe el validador Module para verificar que exista Module antes
     */
    public ValidadorEndModule(ValidadorModule validadorModule) {
        this.validadorModule = validadorModule;
    }

    /*
      Valida la sentencia End Module
     */
    public List<ErrorLexico> validar(
            String linea,
            int numeroLinea,
            boolean esUltimaLinea
    ) {

        List<ErrorLexico> errores = new ArrayList<>();

        String lineaLimpia = linea.trim();

        // Si no es End Module, no se valida aquí
        if (!lineaLimpia.toLowerCase().startsWith("end module")) {
            return errores;
        }

        // Solo puede existir una vez
        if (endModuleEncontrado) {
            errores.add(new ErrorLexico(
                    800,
                    numeroLinea,
                    "La sentencia End Module solo puede declararse una vez"
            ));
            return errores;
        }

        endModuleEncontrado = true;

        // Verificar que Module exista antes
        if (!validadorModule.seDeclaroModule()) {
            errores.add(new ErrorLexico(
                    801,
                    numeroLinea,
                    "End Module aparece sin una declaración previa de Module"
            ));
        }

        // Se separan los tokens manualmente
        String[] partes = lineaLimpia.split(" ");

        if (partes.length != 2) {
            errores.add(new ErrorLexico(
                    802,
                    numeroLinea,
                    "Formato incorrecto. Debe ser exactamente: End Module"
            ));
            return errores;
        }

        // Verificar espacio único
        if (lineaLimpia.contains("  ")) {
            errores.add(new ErrorLexico(
                    803,
                    numeroLinea,
                    "Debe existir exactamente un espacio entre 'End' y 'Module'"
            ));
        }

        // Verificar palabras correctas
        if (!partes[0].equalsIgnoreCase("End")
                || !partes[1].equalsIgnoreCase("Module")) {
            errores.add(new ErrorLexico(
                    804,
                    numeroLinea,
                    "Sentencia incorrecta. Se esperaba: End Module"
            ));
        }

        // Debe ser la última línea del archivo
        if (!esUltimaLinea) {
            errores.add(new ErrorLexico(
                    805,
                    numeroLinea,
                    "End Module debe ser la última línea del archivo"
            ));
        }

        return errores;
    }
}
