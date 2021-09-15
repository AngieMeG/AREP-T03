package edu.escuelaing.arep.networking.webapp;

import edu.escuelaing.arep.networking.springplus.Service;
import edu.escuelaing.arep.networking.springplus.Component;

@Component
public class Math {
    
    @Service("/cuadrado")
    public static Double cuadrado(){
        return 2.0 * 2.0;
    }

    @Service("/cubo")
    public static Double cubo(){
        return 2.0 * 2.0 * 2.0;
    }
}
