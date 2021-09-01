package edu.escuelaing.arep.networking.sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SquareServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
        boolean runnig = true;
        while(runnig){
            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()));
            String inputLine, outputLine;
            double squareNumber;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Mensaje recibido desde el cliente: " + inputLine);
                squareNumber = Math.pow(Integer.parseInt(inputLine), 2);
                outputLine = "Respuesta desde el servidor square: " + squareNumber ;
                out.println(outputLine);
                if (outputLine.equals("Respuesta desde el servidor square: Bye."))
                    break;
            }
            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }
}