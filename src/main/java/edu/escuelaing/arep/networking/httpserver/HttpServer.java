package edu.escuelaing.arep.networking.httpserver;

import java.net.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class HttpServer {
    public static final Integer PORT = 35000;
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

    public static HttpServer getInstance(){
        return _instance;
    }

    private HttpServer(){
        
    }

    public void start() throws IOException, URISyntaxException{
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
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            //System.out.println("Received from the client: " + inputLine);
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

    public String welcomePage() {
        String outputLine = TEXT_MESSAGE_OK.replace("extension", "html");
        outputLine +=     "<!DOCTYPE html>"
                        + "<html>"
                        +       "<head>"
                        +           "<title>HTTP Server</title>\n"
                        +           "<meta charset=\"UTF-8\">"
                        +           "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.8\">"
                        +       "</head>"
                        +       "<body>"
                        +           "<h1>To use the server follow the instructions bellow: </h1>"
                        +           "<ul>"
                        +               "<li> To load the html file add to the url '/html'</li>"
                        +               "<li> To load the img file add to the url '/img'</li>"
                        +               "<li> To load the javaScript file add to the url '/js'</li>"
                        +               "<li> To load the css file add to the url '/css'</li>"
                        +           "</ul>"
                        +       "</body>"
                        + "</html>";
        return outputLine;
    }

    public void computeTextResponse(OutputStream out, String type, String extension){
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

    public void computeImageResponse(OutputStream out, String type, String extension){
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

    public void default404HTMLResponse(OutputStream out){
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
    public static void main(String[] args) throws IOException {
        try {
            HttpServer.getInstance().start();
        } catch (URISyntaxException e) {
            Logger.getLogger(HttpServer.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
