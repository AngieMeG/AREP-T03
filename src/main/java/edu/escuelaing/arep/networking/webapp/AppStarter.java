package edu.escuelaing.arep.networking.webapp;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.escuelaing.arep.networking.httpserver.HttpServer;

/**
 * Class to start the server
 * @author Angie Medina
 * @version 1.0
 */
public class AppStarter {

    public static void main(String... args){
        try {
            HttpServer.getInstance().start();
        } catch (URISyntaxException | IOException e) {
            Logger.getLogger(HttpServer.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
