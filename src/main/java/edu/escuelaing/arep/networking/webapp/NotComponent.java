package edu.escuelaing.arep.networking.webapp;

import edu.escuelaing.arep.networking.springplus.Service;

public class NotComponent {

    @Service("/test")
    public static String test(){
        return "Ejecuto un metodo de una clase que no es componente";
    }
}
