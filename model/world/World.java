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

public class World {
    private static final int MAX_ROWS = 10;
    private static final int MAX_COLUMNS = 10;
    private static final String EMPTY_SPACE = ".";

    private final List<List<String>> worldMap;
    private final PlayerModelManager playerModelManager;
    private final PlayerConnectionManager playerConnectionManager;
    private static final World world = new World();
    //TODO: The world will have access to every Player's model, e.g. Map<Integer, Player> or PlayerModelManager
    //TODO: The map will have access to every Player's socket to communicate with him, e.g. Map<Integer, Socket> or CommunicationManager

    private final Object movingMonitor = new Object();

    private World() {
        // Initializing the world
        worldMap = new ArrayList<>(MAX_ROWS);
        for(int i = 0; i < MAX_ROWS; i++) {
            List<String> row = new ArrayList<>(MAX_COLUMNS);
            for(int j = 0; j < MAX_COLUMNS; j++) {
                row.add(EMPTY_SPACE);
            }
            worldMap.add(row);
        }

        // initilizing the player's model
        playerModelManager = new PlayerModelManager();

        // initializing the player's connection manager
        playerConnectionManager = new PlayerConnectionManager();
    }

    public static World getInstance() {
        return world;
    }

    public void movePlayerInDirection(int playerId, Direction direction) throws IOException {
        Player currentPlayer = playerModelManager.getPlayer(playerId);
        int newX = currentPlayer.getXPos();
        int newY = currentPlayer.getYPos();

        switch(direction) {
            case UP -> newX -= 1;
            case DOWN -> newX += 1;
            case LEFT -> newY -= 1;
            case RIGHT -> newY += 1;
        }

        synchronized (movingMonitor) {
            boolean isValidPosition = isValidPlayerPosition(newX, newY);
            if(isValidPosition) {
                // the last place of the player will be set as EMPTY
                worldMap.get(currentPlayer.getXPos()).set(currentPlayer.getYPos(), EMPTY_SPACE);
                // updating the player itself and
                playerModelManager.updatePlayerPosition(playerId, newX, newY);
                changePlayerLocation(playerId);
            }
        }
    }

    public boolean isValidPlayerPosition(int playerX, int playerY) {
        return (playerX >= 0 && playerX < MAX_ROWS) && (playerY >= 0 && playerY < MAX_COLUMNS);
    }

    //TODO: Create a logic if there is another player
    public void changePlayerLocation(Integer id) throws IOException {
        Player player = playerModelManager.getPlayer(id);
        int playerXPosition = player.getXPos();
        int playerYPosition = player.getYPos();

        if(!worldMap.get(playerXPosition).get(playerYPosition).equals(EMPTY_SPACE)) {
            worldMap.get(playerXPosition).set(playerYPosition, worldMap.get(playerXPosition).get(playerYPosition) + "" + id);
        } else {
            worldMap.get(playerXPosition).set(playerYPosition, "" + id);
        }

        sendMapToAll();
    }

    public void acceptNewPlayer(Socket socketForPlayer, int playerId) throws IOException {

        List<Integer> positionToStart = calculateBestPosition();
        Player player = new Player(
                Player.STARTING_PLAYER_HEALTH, Player.STARTING_PLAYER_MANA, Player.STARTING_PLAYER_DAMAGE, Player.STARTING_PLAYER_DEFENSE,
                Player.PLAYER_STARTING_LEVEL, playerId, positionToStart.getFirst(), positionToStart.getLast());
        playerModelManager.addNewPlayerModel(playerId, player);
        playerConnectionManager.addNewPlayerConnection(playerId, socketForPlayer);
        changePlayerLocation(playerId);



        //TODO: Create new player
        //TODO: Assign the new player a location
        //TODO: Add the player in the player's manager for the world
        //TODO: Add the player's socket in the connection's manager for the world
        //TODO: Update the world to all players
    }

    //TODO: If currently there are no possible place to place the client, inform him
    //TODO: In the future add logic that will calculate location for the player on a position where no enemies are in the 4 directions
    public List<Integer> calculateBestPosition() {
        for(int i = 0; i < worldMap.size(); i++) {
            List<String> column = worldMap.get(i);
            for(int j = 0; j < column.size(); j++) {
                if(column.get(j).equals(EMPTY_SPACE)) {
                    return List.of(i,j);
                }
            }
        }
        return List.of();
    }

    @Override
    public String toString() {
        StringBuilder worldMapString = new StringBuilder();
        for(int i = 0; i < MAX_ROWS; i++) {
            for(int j = 0; j < MAX_COLUMNS; j++) {
                worldMapString.append(worldMap.get(i).get(j)).append("    ");
            }
            worldMapString.append("\n");
        }
        worldMapString.append("\n");

        return worldMapString.toString();
    }

    public void sendMapToAll() throws IOException {
        for(var playerSocket : playerConnectionManager.getActivePlayersConnections().values()) {
            playerSocket.getOutputStream().write(toString().getBytes(StandardCharsets.UTF_8));
        }
    }
}
