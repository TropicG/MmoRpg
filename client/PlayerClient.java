package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PlayerClient {

    private static int SERVER_PORT = 8080;
    private static String SERVER_IP = "localhost";

    public static void main(String[] args) {

        try(Socket socket = new Socket(SERVER_IP, SERVER_PORT);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner sIn = new Scanner(System.in);
            ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {

            WorldMapListener worldMapListener = new WorldMapListener(in);
            executorService.execute(worldMapListener);

            while(true) {
                String inputLine;
                inputLine = sIn.nextLine();
                out.println(inputLine);
            }

        } catch (IOException e) {
            System.out.println("Problem with connecting the client with the server");
            //TODO: Generate a file containing error logs for the error
        }

    }

}
