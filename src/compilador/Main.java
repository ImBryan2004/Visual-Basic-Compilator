package compilador;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        System.out.println("Directorio de ejecución: " + System.getProperty("user.dir"));

        List<String> lineas = new ArrayList<>();

        
         // Buscar archivo .vb en el directorio actual
         
        File carpeta = new File(".");
        File[] archivosVB = carpeta.listFiles((dir, name) -> name.toLowerCase().endsWith(".vb"));

        if (archivosVB == null || archivosVB.length == 0) {
            System.out.println("No se encontró ningún archivo .vb en el directorio actual.");
            return;
        }

        File archivo = archivosVB[0];
        System.out.println("Archivo encontrado: " + archivo.getName());

        
         // Leer archivo línea por línea respetando formato original
         
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {

            String linea;

            while ((linea = br.readLine()) != null) {
                lineas.add(linea);
            }

        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
            return;
        }

        
         // Crear analizador léxico
         
        AnalizadorLexico analizador = new AnalizadorLexico(lineas);

        
         // Ejecutar análisis
         
        List<ErrorLexico> errores = analizador.analizar();

        
         // GENERAR ARCHIVO LOG
         
        try {

            GeneradorLog generadorLog = new GeneradorLog(archivo.getName());

            // Copiar contenido original con numeración
            generadorLog.generarContenidoBase(lineas);

            // Agregar errores al final
            generadorLog.agregarErrores(errores);

            System.out.println("\nArchivo LOG generado: " + generadorLog.getNombreArchivoLog());

        } catch (IOException e) {
            System.out.println("Error al generar el archivo LOG: " + e.getMessage());
        }

        
         // Mostrar resultados en consola
         
        System.out.println("\n=================================");
        System.out.println("RESULTADO DEL ANALISIS LEXICO");
        System.out.println("=================================");

        if (errores.isEmpty()) {
            System.out.println("Compilación exitosa. No se encontraron errores.");
        } else {
            System.out.println("Se encontraron " + errores.size() + " errores:\n");

            for (ErrorLexico error : errores) {
                System.out.println(error);
            }
        }

        
         // Mostrar tabla de símbolos (para pruebas)
         /*
        System.out.println("\n=================================");
        System.out.println("TABLA DE SIMBOLOS");
        System.out.println("=================================");

        analizador.getTablaSimbolos().mostrarTabla();
        */
    }
}
