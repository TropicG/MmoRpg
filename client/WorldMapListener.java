package client;

import java.io.BufferedReader;
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
        while(connectionActive) {
            String currentWorldMap;
            try{
                while((currentWorldMap = in.readLine()) != null) {
                    System.out.println(currentWorldMap);
                }
            } catch (IOException e) {
                System.out.println("Problem with receiving world map from the server");
                //TODO: Generate a file for the client with logs
            }
        }
    }
}
