package edu.escuelaing.arsw.ASE.app;

import java.io.*;
import java.net.*;
import java.nio.file.Files;

/**
 * This class represents a client handler that manages individual client connections
 * for the EchoServerConcurrent. It implements the Runnable interface to allow
 * concurrent execution by the thread pool.
 */
public class ClientDriver implements Runnable {
    private ServerSocket serverSocket;

    public ClientDriver(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    /**
     * The run method is executed when the ClientDriver is submitted to the executor service.
     * It accepts a client connection, handles the request, and closes the client socket.
     */
    @Override
    public void run() {
        Socket clientSocket =  null;
        try {
            clientSocket = serverSocket.accept();
            System.out.println("conection ok");
            request(clientSocket);
        } catch (IOException e) {
            System.err.println("Error Driving client request: " + e.getMessage());
        } finally {
            if (clientSocket != null) {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    System.err.println("Error closing client socket: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Handles an incoming client request by reading HTTP GET requests and sending appropriate responses.
     * @param clientSocket The socket representing the client connection.
     * @throws IOException If an I/O error occurs when reading from or writing to the socket.
     */
    public static void request(Socket clientSocket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
        OutputStream dataOut = clientSocket.getOutputStream();

        String inputLine = in.readLine();
        if (inputLine == null || !inputLine.startsWith("GET")) {
            return;
        }

        String[] requestParts = inputLine.split(" ");
        String filePath = requestParts[1].equals("/") ? "/index.html" : requestParts[1];

        File file = new File("src/main/java/edu/escuelaing/arsw/ASE/app/Files" + filePath);
        if (file.exists() && !file.isDirectory()) {
            String contentType = getContentType(filePath);
            byte[] fileData = Files.readAllBytes(file.toPath());

            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: " + contentType);
            out.println("Content-Length: " + fileData.length);
            out.println();
            out.flush();

            dataOut.write(fileData, 0, fileData.length);
            dataOut.flush();
        } else {
            String errorMessage = "HTTP/1.1 404 Not Found\r\n" +
                                    "Content-Type: text/html\r\n" +
                                    "\r\n" +
                                    "<h1>404 Not Found</h1>";
            out.println(errorMessage);
            out.flush();
        }

        in.close();
        out.close();
        dataOut.close();
        clientSocket.close();
    }


    /**
     * Determines the content type based on the file extension of the requested resource.
     * @param filePath The path of the requested resource.
     * @return The content type (e.g., "text/html", "image/jpeg").
     */
    public static String getContentType(String filePath) {
        if (filePath.endsWith(".html") || filePath.endsWith(".htm")) {
            return "text/html";
        } else if (filePath.endsWith(".jpg") || filePath.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (filePath.endsWith(".png")) {
            return "image/png";
        } else if (filePath.endsWith(".gif")) {
            return "image/gif";
        } else if (filePath.endsWith(".css")) {
            return "text/css";
        } else if (filePath.endsWith(".js")) {
            return "application/javascript";
        } else {
            return "application/indefinite";
        }
    }
}