package edu.escuelaing.arep.networking.network;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * Hello world!
 *
 */
public class URLExamples {
    public static void main( String... args ){
        try {
            URL firstSite = new URL("https://www.google.com/");
            System.out.println("La URL es: " + firstSite.toString());
            System.out.println("El host es: " + firstSite.getHost());
            System.out.println("La puerto es: " + firstSite.getPort());
            System.out.println("La Authority es: " + firstSite.getAuthority());
            System.out.println("La archivo es: " + firstSite.getFile());
            System.out.println("La path es: " + firstSite.getPath());
            System.out.println("La protocolo es: " + firstSite.getProtocol());
            System.out.println("La query es: " + firstSite.getQuery());
            System.out.println("La referencia es: " + firstSite.getRef());

        } catch (MalformedURLException e) {
            Logger.getLogger(URLExamples.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
