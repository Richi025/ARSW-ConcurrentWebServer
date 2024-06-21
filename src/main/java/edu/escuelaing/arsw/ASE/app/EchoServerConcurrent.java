package edu.escuelaing.arsw.ASE.app;
import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class implements a concurrent echo server that handles multiple client requests simultaneously.
 * It uses a thread pool to manage client connections and respond to HTTP GET requests with appropriate content.
 */
public class EchoServerConcurrent {

    private static final int PORT = 8080;
    private static final int SIZE = 11;

    /**
     * Main method that starts the concurrent echo server.
     * Creates a server socket and initializes a thread pool to handle incoming client requests.
     * @param args Command line arguments (not used in this example).
     * @throws IOException If an I/O error occurs when creating the server socket.
     */
    public static void main(String[] args) throws IOException {
        
        ExecutorService executor = Executors.newFixedThreadPool(SIZE);
        
        try(ServerSocket serverSocket = new ServerSocket(PORT);){
            System.out.println("Web server started on port " + PORT);
            while(true){
                try{
                    ClientDriver hilo = new ClientDriver(serverSocket);
                    executor.submit(hilo);
                } catch (Exception e) {
                    System.err.println("Error submitting task to executor service: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Error handling client request: " + e.getMessage());
            System.exit(10);
        }
    }

}
