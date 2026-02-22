
package compilador;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author thebr
 */
public class LectorArchivo {
    
     // Busca un archivo con extensión .vb en el directorio donde se ejecuta el programa
    
    public File obtenerArchivoVB() {

        File directorioActual = new File(System.getProperty("user.dir"));
        File[] archivos = directorioActual.listFiles();

        if (archivos == null) return null;

        for (File archivo : archivos) {
            if (archivo.isFile() && archivo.getName().endsWith(".vb")) {
                return archivo;
            }
        }

        return null;
    }

    
     // Lee el archivo VB respetando completamente su formato
     
    public List<String> leerArchivo(File archivoVB) throws IOException {

        List<String> lineas = new ArrayList<>();

        BufferedReader lector = new BufferedReader(new FileReader(archivoVB));
        String linea;

        while ((linea = lector.readLine()) != null) {
            lineas.add(linea);
        }

        lector.close();
        return lineas;
    }
}
