package edu.escuelaing.arep.networking.httpserver;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpServer {
    public static final Integer PORT = 35000;
    private static final HttpServer _instance = new HttpServer();
    private static final String HTTP_MESSAGE_OK = "HTTP/1.1 200 OK\n"
                                                + "Content-Type: text/html\r\n"
                                                + "\r\n";
    private static final String HTTP_MESSAGE_NOT_FOUND = "HTTP/1.1 404 Not Found\n"
                                                + "Content-Type: text/html\r\n"
                                                + "\r\n";                                                

    public static HttpServer getInstance(){
        return _instance;
    }

    private HttpServer(){}

    public void start(String[] args) throws IOException, URISyntaxException{
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + PORT + ".");
            System.exit(1);
        }
        boolean running = true;
        while(running){
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
            serverConnection(clientSocket);
        }
        serverSocket.close();
    }

    public void serverConnection(Socket clientSocket) throws IOException, URISyntaxException {
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        clientSocket.getInputStream()));
        String inputLine, outputLine;
        ArrayList<String> request = new ArrayList<>();
        while ((inputLine = in.readLine()) != null) {
            System.out.println("Received froom the client: " + inputLine);
            request.add(inputLine);
            if (!in.ready()) {
                break;
            }
        }
        String uriStr = request.get(0).split(" ")[1];
        URI resourceURI = new URI(uriStr);

        System.out.println("URI Path: " + resourceURI.getPath());
        System.out.println("URI Query: " + resourceURI.getQuery());

        if(resourceURI.toString().startsWith("/appuser")){
            outputline = getComponentResource(resourceURI);
        } else{
            outputLine = getResource(resourceURI);
        }
        out.println(outputLine);
        out.close();
        in.close();
        clientSocket.close();
    }
    private String getComponentResource(URI resourceURI) {
        return "null";
    }

    public String getResource(URI resourceURI) throws URISyntaxException{
        System.out.println("Received URI: " + resourceURI);
        return computeHTMLResponse();
    }

    public String computeHTMLResponse(){
        String content = HTTP_MESSAGE_OK;
        /*File file = new File("src/main/resources/public_html/index.html");*/
        File file = new File("src/main/resources/public_html/app.js");
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while((line =  br.readLine()) != null) content += line; 
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public String computeDefaultResponse(){
        String outputLine =
                "HTTP/1.1 200 OK\n"
                        + "Content-Type: text/html\r\n"
                        + "\r\n"
                        + "<!DOCTYPE html>"
                        + "<html>"
                        + "<head>"
                        + "<meta charset=\"UTF-8\">"
                        + "<title>Title of the document</title>\n"
                        + "</head>"
                        + "<body>"
                        + "My Web Site"
                        + "</body>"
                        + "</html>";
        return outputLine;
    }

    public String default404HTMLResponse(){
        String outputline = HTTP_MESSAGE_NOT_FOUND;
        outputline +=     "<!DOCTYPE html>"
                        + "<html>"
                        +       "<head>"
                        +           "<title>404 Not Found</title>\n"
                        +           "<meta charset=\"UTF-8\">"
                        +           "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.8\">"
                        +       "</head>"
                        +       "<bo>"
                        +           "<div>My Web Site</div>"
                        +           "<img src=\"https://ichef.bbci.co.uk/news/640/cpsprodpb/27F1/production/_105952201_9372.jpg"
                        +       "</body>"
                        + "</html>";
        return outputline;
    }
    public static void main(String[] args) throws IOException {
        try {
            HttpServer.getInstance().start(args);
        } catch (URISyntaxException e) {
            Logger.getLogger(HttpServer.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
