package model.world.manager;

import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerConnectionManager {
    private final Map<Integer, Socket> activePlayersConnections;

    public PlayerConnectionManager() {
        this.activePlayersConnections = new ConcurrentHashMap<>();
    }

    public void addNewPlayerConnection(Integer id, Socket socketForPlayer) {
        activePlayersConnections.put(id, socketForPlayer);
    }

    public Map<Integer, Socket> getActivePlayersConnections() {
        return activePlayersConnections;
    }

    public Socket getPlayerSocket(int playerId) {
        return activePlayersConnections.get(playerId);
    }
}
