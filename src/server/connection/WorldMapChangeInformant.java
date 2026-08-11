package server.connection;

import model.world.World;
import model.world.manager.PlayerConnectionManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class WorldMapChangeInformant implements Runnable {

    private final World world;
    private boolean isServerActive;

    public WorldMapChangeInformant(World world, boolean isServerActive) {
        this.world = world;
        this.isServerActive = isServerActive;
    }

    @Override
    public void run() {
        while(isServerActive) {
            if(world.isMapChanged()){
                PlayerConnectionManager playerConnectionManager = world.getPlayerConnectionManager();
                for (var playerSocket : playerConnectionManager.getActivePlayersConnections().values()) {
                    try {
                        playerSocket.getOutputStream().write(world.toString().getBytes(StandardCharsets.UTF_8));
                        playerSocket.getOutputStream().flush();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                world.setMapChanged(false);
            }
        }
    }
}
