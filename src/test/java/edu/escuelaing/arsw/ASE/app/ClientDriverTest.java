package edu.escuelaing.arsw.ASE.app;

import org.junit.jupiter.api.Test;
import java.io.*;
import java.net.*;
import static org.junit.jupiter.api.Assertions.*;

public class ClientDriverTest {

    @Test
    public void testRequestValidFile() throws IOException {

        String request = "GET /index.html HTTP/1.1\r\n";
        InputStream inputStream = new ByteArrayInputStream(request.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Socket socket = new Socket() {
            public InputStream getInputStream() {
                return inputStream;
            }
            public OutputStream getOutputStream() {
                return outputStream;
            }
        };

        ClientDriver.request(socket);
        String response = outputStream.toString();
        assertTrue(response.contains("HTTP/1.1 200 OK"));
        assertTrue(response.contains("Content-Type: text/html"));


        inputStream.close();
        outputStream.close();
        socket.close();
    }

    @Test
    public void testRequestNotFound() throws IOException {
        String request = "GET /nonexistent.html HTTP/1.1\r\n";
        InputStream inputStream = new ByteArrayInputStream(request.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Socket socket = new Socket() {
            public InputStream getInputStream() {
                return inputStream;
            }

            public OutputStream getOutputStream() {
                return outputStream;
            }
        };
        ClientDriver.request(socket);
        String response = outputStream.toString();
        assertTrue(response.contains("HTTP/1.1 404 Not Found"));
        inputStream.close();
        outputStream.close();
        socket.close();
    }
}