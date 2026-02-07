package model.world;

import model.character.player.Player;
import model.world.direction.Direction;
import model.world.manager.PlayerConnectionManager;
import model.world.manager.PlayerModelManager;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class World {
    private static final int MAX_ROWS = 10;
    private static final int MAX_COLUMNS = 10;
    private static final String EMPTY_SPACE = ".";

    private final String[][] worldMap;
    private Set<String> possiblePlayerIds;

    private volatile boolean mapChanged;

    //private final List<List<String>> worldMap;
    private final PlayerModelManager playerModelManager;
    private final PlayerConnectionManager playerConnectionManager;
    private static final World world = new World();
    //TODO: The world will have access to every Player's model, e.g. Map<Integer, Player> or PlayerModelManager
    //TODO: The map will have access to every Player's socket to communicate with him, e.g. Map<Integer, Socket> or CommunicationManager

    private final Object movingMonitor = new Object();

    private World() {
        // Initializing the world
        worldMap = new String[MAX_ROWS][MAX_COLUMNS];
        for (int i = 0; i < MAX_ROWS; i++) {
            for (int j = 0; j < MAX_COLUMNS; j++) {
                worldMap[i][j] = EMPTY_SPACE;
            }
        }

        // initilizing the player's model
        playerModelManager = new PlayerModelManager();

        // initializing the player's connection manager
        playerConnectionManager = new PlayerConnectionManager();

        possiblePlayerIds = Set.of("1", "2", "3", "4", "5", "6", "7", "8", "9");
        mapChanged = false;
    }

    public static World getInstance() {
        return world;
    }

    public void movePlayerInDirection(int playerId, Direction direction) throws IOException {
        Player player = playerModelManager.getPlayer(playerId);
        int newX = player.getXPos();
        int newY = player.getYPos();

        switch (direction) {
            case UP -> newX -= 1;
            case DOWN -> newX += 1;
            case LEFT -> newY -= 1;
            case RIGHT -> newY += 1;
        }

        synchronized (movingMonitor) {
            boolean isValidPosition = isValidPlayerPosition(newX, newY);
            if (isValidPosition) {
                // saving the old location
                updateCurrentLoc(String.valueOf(playerId), player.getXPos(), player.getYPos());
                playerModelManager.updatePlayerPosition(playerId, newX, newY); // updating the player new location
                changePlayerLocation(playerId, newX, newY); // actual change of the player location
            }
        }
    }

    // TODO: The player cannot go to a place with 2 players or 1 player and 1 Minion
    private boolean isValidPlayerPosition(int playerX, int playerY) {
        return ((playerX >= 0 && playerX < MAX_ROWS) && (playerY >= 0 && playerY < MAX_COLUMNS)) &&
                (worldMap[playerX][playerY].length() != 2);
    }

    private boolean hasTileAnotherPlayer(int playerXPos, int playerYPos) {
        return possiblePlayerIds.contains(worldMap[playerXPos][playerYPos]);
    }

    private void updateCurrentLoc(String playerId, int playerXPos, int playerYPos) {
        // if the current location has two characters on the tile
        if(worldMap[playerXPos][playerYPos].length() == 2) {
            worldMap[playerXPos][playerYPos] = worldMap[playerXPos][playerYPos].replaceFirst(playerId,"");
        } else {
            worldMap[playerXPos][playerYPos] = EMPTY_SPACE;
        }
    }

    //TODO: Create a logic if there is another player
    public void changePlayerLocation(int id, int playerXPos, int playerYPos) throws IOException {
        if (hasTileAnotherPlayer(playerXPos, playerYPos)) {
            // if there was another player here, the tile will hold the two players
            worldMap[playerXPos][playerYPos] = worldMap[playerXPos][playerYPos] + id;
        } else {
            // if the tile that the player has to move is empty
            worldMap[playerXPos][playerYPos] = "" + id;
        }

        setMapChanged(true);
        //sendMapToAll();
    }

    public void acceptNewPlayer(Socket socketForPlayer, int playerId) throws IOException {

        synchronized (movingMonitor) {
            List<Integer> positionToStart = calculateBestPosition();
            Player player = new Player(
                    Player.STARTING_PLAYER_HEALTH, Player.STARTING_PLAYER_MANA, Player.STARTING_PLAYER_DAMAGE, Player.STARTING_PLAYER_DEFENSE,
                    Player.PLAYER_STARTING_LEVEL, playerId, positionToStart.getFirst(), positionToStart.getLast());
            playerModelManager.addNewPlayerModel(playerId, player);
            playerConnectionManager.addNewPlayerConnection(playerId, socketForPlayer);
            changePlayerLocation(playerId, player.getXPos(), player.getYPos());
        }

        //TODO: Create new player
        //TODO: Assign the new player a location
        //TODO: Add the player in the player's manager for the world
        //TODO: Add the player's socket in the connection's manager for the world
        //TODO: Update the world to all players
    }

    //TODO: If currently there are no possible place to place the client, inform him
    //TODO: In the future add logic that will calculate location for the player on a position where no enemies are in the 4 directions
    public List<Integer> calculateBestPosition() {
        for (int i = 0; i < MAX_ROWS; i++) {
            for (int j = 0; j < MAX_COLUMNS; j++) {
                if (worldMap[i][j].equals(EMPTY_SPACE)) {
                    return List.of(i, j);
                }
            }
        }
        return List.of();
    }

    // The world map will be transformed to String so that It can be send to all the clients
    @Override
    public String toString() {
        StringBuilder worldMapString = new StringBuilder();
        for (int i = 0; i < MAX_ROWS; i++) {
            for (int j = 0; j < MAX_COLUMNS; j++) {
                worldMapString.append(worldMap[i][j]).append("    ");
            }
            worldMapString.append("\n");
        }
        worldMapString.append("\n");

        return worldMapString.toString();
    }

    // the worldMap is sending information to all its player
    // TODO: Create a thread that will run on the server side that will check if the world map has new information so that it sends data to the clients
    public void sendMapToAll() throws IOException {
        for (var playerSocket : playerConnectionManager.getActivePlayersConnections().values()) {
            playerSocket.getOutputStream().write(toString().getBytes(StandardCharsets.UTF_8));
        }
    }

    public void setMapChanged(boolean mapChanged) {
        this.mapChanged = mapChanged;
    }

    public boolean isMapChanged() {
        return this.mapChanged;
    }

    public PlayerConnectionManager getPlayerConnectionManager() {
        return playerConnectionManager;
    }
}
