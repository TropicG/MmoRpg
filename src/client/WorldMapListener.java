package client;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;

public class WorldMapListener implements Runnable {
    private BufferedReader in;
    private boolean connectionActive;


    public WorldMapListener(BufferedReader in) {
        this.in = in;
        connectionActive = true;
    }

    @Override
    public void run() {
        while (connectionActive) {
            String currentWorldMap;
            try {
                while ((currentWorldMap = in.readLine()) != null) {
                    System.out.println(currentWorldMap);
                }
            } catch (IOException e) {
                System.out.println("Connection has died, open errorWorldCannotLoadLogs for information");
                try (FileWriter fileWriter = new FileWriter("errorWorldCannotLoadLogs.txt")) {
                    fileWriter.write("Problem with connecting the client with the server");
                    connectionActive = false;
                    return;
                } catch (IOException ioException) {
                    System.out.println("Problems with opening a file");
                }
            }
        }
    }
}
