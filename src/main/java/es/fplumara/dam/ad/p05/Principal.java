package es.fplumara.dam.ad.p05;

import es.fplumara.dam.ad.p05.modelo.Zapatilla;
import es.fplumara.dam.ad.p05.util.FicheroInventario;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Principal {

    public static void main(String[] args) {

        //Paso 2
        Zapatilla prueba = new Zapatilla(0, "Prueba", "Sin marca", "casual", 42.0, 10.0, 1);
        System.out.println("Zapatilla de prueba: " + prueba);

        //Paso 3
        System.out.println("Tamaño de un registro: " + FicheroInventario.TAMANO_REGISTRO + " bytes");
        System.out.println("El registro 2 empieza en el byte " + FicheroInventario.posicion(2));

        //Paso 5
        FicheroInventario fichero = new FicheroInventario("datos/inventario.dat");
        try{
            int cargados = fichero.crearDesdeCsv("datos/zapatillas.csv");
            System.out.println("Registros cargados desde el CSV: " + cargados);
            System.out.println("Tamaño de inventario.dat: " + Files.size(Path.of("datos/inventario.dat")) + " bytes");
        } catch (IOException e) {
            System.out.println("Error de entrada/salida: " + e.getMessage());
        }
    }
}
