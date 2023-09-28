package org.clevertec;

import lombok.Setter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private static final int PORT = (int)OpenConfig.openFile().get("PORT");
    private static final String ADDRESS = (String)OpenConfig.openFile().get("ADDRESS");
    @Setter
    protected static boolean running = false;
    private static ExecutorService executorService;
    private final static int poolSize = Runtime.getRuntime().availableProcessors();

    public static void start() {
        try (ServerSocket server = new ServerSocket(PORT, 50, InetAddress.getByName(ADDRESS))) {
            executorService = Executors.newFixedThreadPool(poolSize);
            System.out.println("Server started!");
            running = true;

            while (running) {
                Session session = new Session(server);
                executorService.submit(session);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            executorService.shutdown();
        }
    }
}
