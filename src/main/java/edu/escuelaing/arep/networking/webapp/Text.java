package edu.escuelaing.arep.networking.webapp;

import edu.escuelaing.arep.networking.springplus.Service;
import edu.escuelaing.arep.networking.springplus.Component;

@Component
public class Text {
    @Service("/greetings")
    public static String greetings(){
        return "Hello from the implementation of Spring";
    }
}
