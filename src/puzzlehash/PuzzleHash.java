/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzlehash;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author furid
 */
public class PuzzleHash {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String texto = "Hola Mundo";
        String criterio = "[0]{2}.*";
        
        SearchHash hilo1 = new SearchHash(texto, criterio, 0, "Secuencial");
        hilo1.start();
        SearchHashConcurrente hilo01 = new SearchHashConcurrente(texto, criterio, 0, "Concurrente", 4);
        hilo01.start();

    }
}
