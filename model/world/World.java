package model.world;

import model.character.player.Player;
import model.world.direction.Direction;
import model.world.manager.PlayerConnectionManager;
import model.world.manager.PlayerModelManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

public class World {
    private static final int MAX_ROWS = 10;
    private static final int MAX_COLUMNS = 10;
    private static final String EMPTY_SPACE = ".";

    private static final World world = new World();

    private final PlayerModelManager playerModelManager;
    private final PlayerConnectionManager playerConnectionManager;
    private final String[][] worldMap;
    private Set<String> possiblePlayerIds;
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
    }

    public static World getInstance() {
        return world;
    }

    // THESE FUNCTIONS ARE RESPONSIBLE FOR MOVING A SPECIFIC PLAYER
    public void movePlayerInDirection(int playerId, Direction direction) {
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
            System.out.println("Moving to direction " + direction.toString());
            boolean isValidPosition = isValidPlayerPosition(newX, newY);
            if (isValidPosition) {
                // changes the current tile the player is on, either leave it * or check if there is another player on the tile
                updateCurrentLoc(String.valueOf(playerId), player.getXPos(), player.getYPos());
                playerModelManager.updatePlayerPosition(playerId, newX, newY); // updating the player new location
                changePlayerLocation(playerId, newX, newY); // actual change of the player location
            }
        }
    }

    // TODO: The player cannot go to a place with 2 players or 1 player and 1 Minion
    private boolean isValidPlayerPosition(int newX, int newY) {
        return ((newX >= 0 && newX < MAX_ROWS) && (newY >= 0 && newY < MAX_COLUMNS)) &&
                (worldMap[newX][newY].length() != 2);
    }

    private boolean hasTileAnotherPlayer(int newX, int newY) {
        return possiblePlayerIds.contains(worldMap[newX][newY]);
    }

    private void updateCurrentLoc(String playerId, int playerX, int playerY) {
        // if the current location has two characters on the tile
        if (worldMap[playerX][playerY].length() == 2) {
            worldMap[playerX][playerY] = worldMap[playerX][playerY].replaceFirst(playerId, "");
        } else {
            worldMap[playerX][playerY] = EMPTY_SPACE;
        }
    }

    //TODO: Create a logic if there is another player
    public void changePlayerLocation(int id, int newX, int newY) {
        if (hasTileAnotherPlayer(newX, newY)) {
            // if there was another player here, the tile will hold the two players
            worldMap[newX][newY] = worldMap[newX][newY] + id;
        } else {
            // if the tile that the player has to move is empty
            worldMap[newX][newY] = "" + id;
        }

        sendMapToAll();
    }

    // THESE FUNCTIONS ARE RESPONSIBLE FOR SPAWNING A NEW PLAYER ON THE MAP
    public void acceptNewPlayer(Socket socketForPlayer, int playerId) {

        synchronized (movingMonitor) {
            List<Integer> positionToStart = calculateBestPosition();
            Player player = new Player(
                    Player.STARTING_PLAYER_HEALTH, Player.STARTING_PLAYER_MANA, Player.STARTING_PLAYER_DAMAGE, Player.STARTING_PLAYER_DEFENSE,
                    Player.PLAYER_STARTING_LEVEL, playerId, positionToStart.getFirst(), positionToStart.getLast());
            playerModelManager.addNewPlayerModel(playerId, player);
            playerConnectionManager.addNewPlayerConnection(playerId, socketForPlayer);
            changePlayerLocation(playerId, player.getXPos(), player.getYPos());
        }

        informPlayer(playerId, "Welcome to the server player: " + playerId + "\n");

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

    //THESE FUNCTIONS ARE GOING TO BE RESPONSIBLE FOR INFORMING THE PLAYER
    public void informPlayer(int playerId, String message) {
        Socket playerSocket = playerConnectionManager.getPlayerSocket(playerId);

        try {
            playerSocket.getOutputStream().write(message.getBytes(StandardCharsets.UTF_8));
            playerSocket.getOutputStream().flush();
        } catch (IOException e) {
            System.out.println("Problems when informing the player");
            //TODO: Creating a file on the server side file with lgos
        }
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
    public void sendMapToAll() {
        for (var playerSocket : playerConnectionManager.getActivePlayersConnections().values()) {
            try {
                playerSocket.getOutputStream().write(toString().getBytes(StandardCharsets.UTF_8));
                playerSocket.getOutputStream().flush();
            } catch (IOException ioException) {
                System.out.println("Problem when sending the world map to the client");
                //TODO: Make the logs file on the server side
            }
        }
    }

    public PlayerConnectionManager getPlayerConnectionManager() {
        return playerConnectionManager;
    }
}
