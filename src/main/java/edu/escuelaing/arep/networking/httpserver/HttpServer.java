package edu.escuelaing.arep.networking.httpserver;

import java.net.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Implementacion de un servidor web, este recibe peticiones HTTP y entrega
 * recursos HTML, JS, CSS e imagenes
 * @author Angie Medina
 * @version 3.0 (06/09/21)
 */
public class HttpServer {
    private static final HttpServer _instance = new HttpServer();
    private static final HashMap<String, String> extensionHeaders = new HashMap<String, String>(){{
        put("html", "text");
        put("js", "text");
        put("css", "text");
        put("PNG", "image");
        put("png", "image");
        put("jpg", "image");
    }};

    private static final String TEXT_MESSAGE_OK = "HTTP/1.1 200 OK\n"
                                                + "Content-Type: text/extension\r\n"
                                                + "\r\n";

    private static final String HTTP_MESSAGE_NOT_FOUND = "HTTP/1.1 404 Not Found\n"
                                                + "Content-Type: text/html\r\n"
                                                + "\r\n";

    private static final String IMAGE_MESSAGE = "HTTP/1.1 200 OK\n"
                                            + "Content-Type: image/PNG \r\n"
                                            + "\r\n";

    /**
     * Gets the current instance of the server
     * @return the current instance of the server
     */
    public static HttpServer getInstance(){
        return _instance;
    }

    /**
     * Class constructor
     */
    private HttpServer(){
        
    }

    /**
     * Begin to listen for multiusers requests in the port 35000
     * @throws IOException If the server is unable to listen for the current port 
     * @throws URISyntaxException If the formed URI is incorrect
     */
    public void start() throws IOException, URISyntaxException{
        ServerSocket serverSocket = null;
        int port = getPort();
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + port + ".");
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

    /**
     * Listen and handle the received requests
     * @param clientSocket current socket of the server
     * @throws IOException Any conflict related to the socket
     */
    public void serverConnection(Socket clientSocket) throws IOException {
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        clientSocket.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            if (inputLine.startsWith("GET")){
                System.out.println("Received from the client: " + inputLine);
                manageResource(clientSocket.getOutputStream(), inputLine);
            }
            if (!in.ready()) {
                break;
            }
        }
        out.close();
        in.close();
        clientSocket.close();
    }

    /**
     * Decide what kind of petion was received and handled according to its Content-Type
     * @param out the stream the resources need to display on the client
     * @param input the request
     */
    public void manageResource(OutputStream out, String input){
        String type = input.split(" ")[1].replace("/", "");
        if(type.length() == 0) type = "index.html";
        String fileExtension = type.substring(type.lastIndexOf(".") + 1);
        if(extensionHeaders.containsKey(fileExtension)){
            if(extensionHeaders.get(fileExtension).equals("text")){
                computeTextResponse(out, type, fileExtension);
            }else{
                computeImageResponse(out, type, fileExtension);
            }
        }
    }

    /**
     * Read and write on screen the text resorce
     * @param out the stream the resources need to display on the client
     * @param type the request
     * @param extension the resource's extension
     */
    private void computeTextResponse(OutputStream out, String type, String extension){
        if (extension.equals("js")) extension = "javascript";
        String content = TEXT_MESSAGE_OK.replace("extension", extension);
        File file = new File("src/main/resources/static/"+type);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while((line =  br.readLine()) != null) content += line; 
            br.close();
            out.write(content.getBytes());
        } catch (IOException e) {
            System.err.format("FileNotFoundException %s%n", e);
            default404HTMLResponse(out);
        }
    }

    /**
     * Read and write on screen the image resorce
     * @param out the stream the resources need to display on the client
     * @param type the request
     * @param extension the resource's extension
     */
    private void computeImageResponse(OutputStream out, String type, String extension){
        try{
            BufferedImage image = ImageIO.read(new File("src/main/resources/static/img/"+type));   
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DataOutputStream writeImg = new DataOutputStream(out);
            ImageIO.write(image, extension, bos);
            writeImg.writeBytes(IMAGE_MESSAGE.replace("extension", extension));
            writeImg.write(bos.toByteArray());
        } catch (IOException e){
            default404HTMLResponse(out);
        }
    }


    /**
     * Display a simple html page of 404 Error
     * @param out the stream the resource need to display on the client
     */
    private void default404HTMLResponse(OutputStream out){
        String outputline = HTTP_MESSAGE_NOT_FOUND;
        outputline +=     "<!DOCTYPE html>"
                        + "<html>"
                        +       "<head>"
                        +           "<title>404 Not Found</title>\n"
                        +           "<meta charset=\"UTF-8\">"
                        +           "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.8\">"
                        +       "</head>"
                        +       "<body>"
                        +           "<div>Error 404</div>"
                        +       "</body>"
                        + "</html>";
        try {
            out.write(outputline.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method reads the default port as specified by the PORT variable in
     * the environment.
     *
     * Heroku provides the port automatically so you need this to run the
     * project on Heroku.
     */
    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 35000; //returns default port if heroku-port isn't set (i.e. on localhost)
    }

    public static void main(String[] args) throws IOException {
        try {
            HttpServer.getInstance().start();
        } catch (URISyntaxException e) {
            Logger.getLogger(HttpServer.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
