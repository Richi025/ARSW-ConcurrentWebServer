package edu.escuelaing.arsw.ASE.app;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.net.Socket;
import static org.junit.jupiter.api.Assertions.*;

public class EchoServerConcurrentTest {

    private Thread serverThread;

    @BeforeEach
    public void setUp() {
        serverThread = new Thread(() -> {
            try {
                EchoServerConcurrent.main(null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        serverThread.start();
    }

    @AfterEach
    public void tearDown() {
        serverThread.interrupt();
    }

    @Test
    public void testServerStart() throws IOException {
        Socket clientSocket = new Socket("localhost", 8080);
        assertTrue(clientSocket.isConnected());
        clientSocket.close();
    }
}
