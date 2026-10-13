package es.fplumara.dam.ad.p05.util;

import es.fplumara.dam.ad.p05.modelo.Zapatilla;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase sirve para guardar el inventario de un fichero de registros de longitud fija
 * y permite leer y modificar cualquier registro con acceso aleatorio.
 */

public class FicheroInventario {

    //Longitud del texto en caracteres
    public static final int LONGITUD_MODELO = 20;
    public static final int LONGITUD_MARCA = 15;
    public static final int LONGITUD_CATEGORIA = 12;

    //Tamaño de un registro en bytes
    /**Codigo (int: 4) + tres textos (2 bytes por caracter) + talla y precio
     * (double: 8) + stock (int: 4)
     */
    public static final int TAMANO_REGISTRO = 4 + LONGITUD_MODELO * 2 + LONGITUD_MARCA * 2 + LONGITUD_CATEGORIA * 2 + 8 + 8 +4;

    private String ruta;

    public FicheroInventario(String ruta) {
        this.ruta = ruta;
    }

    /**Devuelve el byte donde empieza el registro número n (el primero es el 1)
     *
     */
    public static long posicion(int numero){
        return (long) (numero - 1) * TAMANO_REGISTRO;
    }

    /**Este metodo se asegura de que cualquier registro ocupe los caracteres necesarios.
     * Si es muy largo lo acorta, si es muy corto lo rellena con espacios
     */
    public static void escribirTexto(RandomAccessFile raf, String texto, int longitud) throws IOException{
        StringBuilder sb = new StringBuilder(texto);
        if (sb.length() > longitud) {
            sb.setLength(longitud);
        }
        while (sb.length() < longitud) {
            sb.append(" ");
        }
        raf.writeChars(sb.toString());
    }

    /**
     * Este metodo escribe una zapatilla a partir de la posicion actual del puntero
     */
    public static void escribirRegistro(RandomAccessFile raf, Zapatilla z) throws IOException{

        raf.writeInt(z.getCodigo());
        escribirTexto(raf, z.getModelo(), LONGITUD_MODELO);
        escribirTexto(raf, z.getMarca(), LONGITUD_MARCA);
        escribirTexto(raf, z.getCategoria(), LONGITUD_CATEGORIA);
        raf.writeDouble(z.getTalla());
        raf.writeDouble(z.getPrecio());
        raf.writeInt(z.getStock());
    }

    /**
     * Este metodo crea el fichero de registros a partir del csv.
     * Si ya existia el fichero de registros, lo vacía antes.
     * Despues, devuelde el número de registros escritos.
     */
    public int crearDesdeCsv(String rutaCsv) throws IOException {
        int contador = 0;
        try (BufferedReader lector = new BufferedReader(new FileReader(rutaCsv,
                StandardCharsets.UTF_8));
             RandomAccessFile raf = new RandomAccessFile(ruta, "rw")) {
            raf.setLength(0);
            lector.readLine(); // se salta la cabecera
            String linea = lector.readLine();
            while (linea != null) {
                String[] campos = linea.split(";");
                Zapatilla z = new Zapatilla(
                        Integer.parseInt(campos[0]),
                        campos[1],
                        campos[2],
                        campos[3],
                        Double.parseDouble(campos[4]),
                        Double.parseDouble(campos[5]),
                        Integer.parseInt(campos[6]));
                escribirRegistro(raf, z);
                contador++;
                linea = lector.readLine();
            }
        }
        return contador;

    }

}
