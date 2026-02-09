package model.world;

import model.character.Actor;
import model.character.enemy.Minion;
import model.character.enemy.MinionFactory;
import model.character.position.Position;
import model.character.player.Player;
import model.treasure.Treasure;
import model.treasure.weapon.Weapon;
import model.world.direction.Direction;
import model.world.manager.MinionManager;
import model.world.manager.PlayerConnectionManager;
import model.world.manager.PlayerModelManager;
import model.world.manager.TreasureManager;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

public class World {
    private static final int MAX_ROWS = 10;
    private static final int MAX_COLUMNS = 10;

    // possible tiles on the map
    public static final String BLOCKAGE_SPACE = "#";
    public static final String EMPTY_SPACE = ".";
    public static final String TREASURE_SPACE = "*";
    public static final String MINION_SPACE = "M";

    private static final int SINGLE_TILE = 1;

    private static final double PERCENTAGE_FOR_BLOCKAGE = 0.20;
    private static final double PERCENTAGE_FOR_TREASURE = 0.10;
    private static final double PERCENTAGE_FOR_MINION = 0.05;

    private static final World world = new World();

    //managers
    private final PlayerModelManager playerModelManager;
    private final PlayerConnectionManager playerConnectionManager;
    private final TreasureManager treasureManager;
    private final MinionManager minionManager;

    //TODO: Make sure the worldMap is StringBuilder
    private final String[][] worldMap;
    private final Set<Character> possiblePlayerIds;

    // monitoring object
    private final Object mapChangeMonitor = new Object();

    // Functions for initializing the World
    private World() {
        minionManager = new MinionManager();

        // Initializing the world
        worldMap = new String[MAX_ROWS][MAX_COLUMNS];
        for (int i = 0; i < MAX_ROWS; i++) {
            for (int j = 0; j < MAX_COLUMNS; j++) {
                if (canPlaceBlockage()) {
                    worldMap[i][j] = BLOCKAGE_SPACE;
                } else if (canPlaceTreasure()) {
                    worldMap[i][j] = TREASURE_SPACE;
                } else if(canPlaceMinion()) {
                    minionManager.addMinionToWorld(Position.of(i,j), MinionFactory.ofOrc(i,j));
                    worldMap[i][j] = MINION_SPACE;
                } else {
                    worldMap[i][j] = EMPTY_SPACE;
                }
            }
        }

        // initilizing the player's model
        playerModelManager = new PlayerModelManager();

        // initializing the treasureManager
        treasureManager = new TreasureManager();

        // initializing the player's connection manager
        playerConnectionManager = new PlayerConnectionManager();

        possiblePlayerIds = Set.of('1', '2', '3', '4', '5', '6', '7', '8', '9');
    }

    // there is a percent chance that a blockage is spawned to the worldMap
    private boolean canPlaceBlockage() {
        return Math.random() < PERCENTAGE_FOR_BLOCKAGE;
    }

    private boolean canPlaceTreasure() {
        return Math.random() < PERCENTAGE_FOR_TREASURE;
    }

    private boolean canPlaceMinion() {
        return Math.random() < PERCENTAGE_FOR_MINION;
    }

    public static World getInstance() {
        return world;
    }

    // THESE FUNCTIONS ARE RESPONSIBLE FOR EQUIPPING A WEAPON TO THE PLAYER
    public void equipWeaponToPlayer(int playerId, Weapon weapon) {
        if(weapon == null) {
            informPlayer(playerId, "This is not a weapon\n");
            return;
        }

        synchronized (mapChangeMonitor) {
            Player player = playerModelManager.getPlayer(playerId);

            if (!player.hasTreasure(weapon)) {
                informPlayer(playerId, "This item is not in the backpack\n");
            } else if (player.getCurrentLevel() < weapon.getLevelReq()) {
                informPlayer(playerId, "The required level for this item is " + weapon.getLevelReq() + "\n");
            } else {
                player.equipWeapon(weapon);
                informPlayer(playerId, "Equiped " + weapon.getTreasureName() + "\n");
            }
        }
    }

    // THESE FUNCTIONS ARE RESPONSIBLE FOR ATTACKING A MINION
    public void attackMinion(int playerId) {
        synchronized (mapChangeMonitor) {

            Player player = playerModelManager.getPlayer(playerId);
            if(!isThereMinion(player.getXPos(), player.getYPos())) {
                informPlayer(playerId, "You are not on minion block\n");
                return;
            }

            Minion minion = minionManager.getMinion(Position.of(player.getXPos(), player.getYPos()));
            minion.takeDamage(player.calculateAtkDmg());
            informPlayer(playerId, "Attack landed\n");
            if(minion.isDead()) {
                informPlayer(playerId, "You killed the minion\n");
                removeMinionFromMap(player.getXPos(), player.getYPos());
                Position newMinionPosition = calculateBestPosition();

                // adding the new minion to the map
                minionManager.addMinionToWorld(newMinionPosition, MinionFactory.ofOrc(newMinionPosition.getX(), newMinionPosition.getY()));
                addMinionOnMap(newMinionPosition.getX(), newMinionPosition.getY());
                sendMapToAll();
                return;
            }

            //TODO: FINISH PLAYER TAKING DAMAGE
            //player.takeDamage(minion.getDamage());
        }
    }

    private boolean areOnSamePosition(Actor player, Actor minion) {
        return player.getXPos() == minion.getXPos() && player.getYPos() == minion.getYPos();
    }

    // THESE FUNCTIONS ARE RESPONSIBLE FOR DROPPING A TREASURE ON THE MAP
    public void dropTreasureFromPlayer(int playerId, Treasure treasure) {
        synchronized (mapChangeMonitor) {
            Player player = playerModelManager.getPlayer(playerId);

            if (!player.hasTreasure(treasure)) {
                informPlayer(playerId, "You do not have this item\n");
            } else {
                player.removeTreasure(treasure);
                addTreasureOnMap(player.getXPos(), player.getYPos());
                sendMapToAll();
            }
        }
    }

    private void addTreasureOnMap(int x, int y) {
        worldMap[x][y] = worldMap[x][y] + TREASURE_SPACE;
    }

    private void addMinionOnMap(int x, int y) {
        worldMap[x][y] = MINION_SPACE;
    }

    // THESE FUNCTIONS ARE RESPONSIBLE FOR MOVING A SPECIFIC PLAYER
    public void movePlayerInDirection(int playerId, Direction direction) {
        // gets the player that shall be moved
        Player player = playerModelManager.getPlayer(playerId);
        int newX = player.getXPos();
        int newY = player.getYPos();

        // calculating the new change to the position
        switch (direction) {
            case UP -> newX -= 1;
            case DOWN -> newX += 1;
            case LEFT -> newY -= 1;
            case RIGHT -> newY += 1;
        }

        boolean hasChangeOccurred = false;

        // thread-safe: only one player connection can change the map at a time
        synchronized (mapChangeMonitor) {
            boolean isValidPosition = isValidPlayerPosition(newX, newY);
            if (isValidPosition) {

                hasChangeOccurred = true;

                //TODO: A player cannot take a * if the backpack is full
                //TODO: Another player can take the * if the other player on tie has its backpack full

                if (isThereTreasure(newX, newY)) {
                    if (!player.isBackpackFull()) {
                        Treasure treasure = treasureManager.getRandomTreasure();
                        playerModelManager.addTreasureToPlayer(playerId, treasure);
                        informPlayer(playerId, "You have received " + treasure.getTreasureName() + "\n");

                        // if the player just got its 10th backpack fill
                        if (player.isBackpackFull()) {
                            removeTreasureFromMap(newX, newY);
                        }
                    }
                }

                // changes the current tile the player is on, either leave it * or check if there is another player on the tile
                updateCurrentLoc(String.valueOf(playerId), player.getXPos(), player.getYPos());
                playerModelManager.updatePlayerPosition(playerId, newX, newY); // updating the player new location
                changePlayerLocation(playerId, player, newX, newY); // actual change of the player location
            }
        }

        if (hasChangeOccurred) {
            sendMapToAll();
        }
    }

    // TODO: The player cannot go to a place with 2 players or 1 player and 1 Minion
    private boolean isValidPlayerPosition(int newX, int newY) {
        return isInsideBound(newX, newY) && !isThereBlockage(newX, newY);
        //return isInsideBound(newX,newY) && (worldMap[newX][newY].length() != 2) && !isThereBlockage(newX,newY);
    }

    private void removeTreasureFromMap(int x, int y) {
        // if only treasure add emptySpace
        if (worldMap[x][y].length() == SINGLE_TILE) {
            worldMap[x][y] = EMPTY_SPACE;
        } else {
            worldMap[x][y] = worldMap[x][y].replace(TREASURE_SPACE, "");
        }
    }

    private void removeMinionFromMap(int x, int y) {
        worldMap[x][y] = worldMap[x][y].replace(MINION_SPACE, "");
    }

    private boolean isThereTreasure(int x, int y) {
        if (worldMap[x][y].length() == SINGLE_TILE) {
            return worldMap[x][y].equals(TREASURE_SPACE);
        }

        for (int i = 0; i < worldMap[x][y].length(); i++) {
            if (worldMap[x][y].charAt(i) == TREASURE_SPACE.charAt(0)) {
                return true;
            }
        }

        return false;
    }

    private boolean isThereMinion(int x, int y) {
        if (worldMap[x][y].length() == SINGLE_TILE) {
            return worldMap[x][y].equals(MINION_SPACE);
        }

        for (int i = 0; i < worldMap[x][y].length(); i++) {
            if (worldMap[x][y].charAt(i) == MINION_SPACE.charAt(0)) {
                return true;
            }
        }

        return false;
    }

    private boolean isThereBlockage(int x, int y) {
        return worldMap[x][y].equals(BLOCKAGE_SPACE);
    }

    private boolean isInsideBound(int x, int y) {
        return ((x >= 0 && x < MAX_ROWS) && (y >= 0 && y < MAX_COLUMNS));
    }

    private boolean arePlayerOnSameLocation(Player playerOne, Player playerTwo) {
        return playerOne.getXPos() == playerTwo.getXPos() && playerOne.getYPos() == playerTwo.getYPos();
    }

    private boolean hasTileAnotherPlayer(int newX, int newY) {
        boolean otherPlayersOnTile = false;
        String tile = worldMap[newX][newY];

        for (int i = 0; i < tile.length(); i++) {
            if (possiblePlayerIds.contains(tile.charAt(i))) {
                otherPlayersOnTile = true;
                break;
            }
        }

        return otherPlayersOnTile;
    }

    private void updateCurrentLoc(String playerId, int playerX, int playerY) {
        // if the current location has two characters on the tile
        if (worldMap[playerX][playerY].length() != 1) {
            worldMap[playerX][playerY] = worldMap[playerX][playerY].replaceFirst(playerId, "");
        } else {
            worldMap[playerX][playerY] = EMPTY_SPACE;
        }
    }

    //TODO: Create a logic if there is another player
    public void changePlayerLocation(int playerid, Player player, int newX, int newY) {
        if (hasTileAnotherPlayer(newX, newY) && isThereTreasure(newX, newY) && !player.isBackpackFull()) {
            // if there is another player on the map and a treasure to take, remove the treasure and add the second player
            // to the player
            removeTreasureFromMap(newX, newY);
            worldMap[newX][newY] = worldMap[newX][newY] + playerid;
        } else if (hasTileAnotherPlayer(newX, newY) || (isThereTreasure(newX, newY) && player.isBackpackFull()) || isThereMinion(newX, newY) ) {
            // if there was another player here, the tile will hold the two players
            // otherwise if there is a treasure and the player cannot take it, on the tile it will combine them
            worldMap[newX][newY] = worldMap[newX][newY] + playerid;
        } else {
            // if the tile that the player has to move is empty
            worldMap[newX][newY] = "" + playerid;
        }
    }

    // THESE FUNCTIONS ARE RESPONSIBLE FOR WRITING THE INVENTORY TO THE CLIENT
    public void showInventoryToPlayer(int playerId) {
        Player player = playerModelManager.getPlayer(playerId);

        if (player.isBackpackEmpty()) {
            System.out.println("BACK PACK IS EMPTY");
            informPlayer(playerId, "Backpack is empty\n");
        } else {
            informPlayer(playerId, player.getBackpack().toString());
        }
    }

    private static int getPlayerId(int playerId) {
        return playerId;
    }

    // THESE FUNCTIONS ARE RESPONSIBLE FOR TRADING BETWEEN PLAYERS
    public void giveTreasureToPlayer(int fromPlayerId, int toPlayerId, Treasure treasure) {

        if (treasure == null) {
            informPlayer(fromPlayerId, "No such item\n");
        }

        synchronized (mapChangeMonitor) {
            Player fromPlayer = playerModelManager.getPlayer(fromPlayerId);
            Player toPlayer = playerModelManager.getPlayer(toPlayerId);

            if (!arePlayerOnSameLocation(fromPlayer, toPlayer)) {
                informPlayer(fromPlayerId, "You are not on the same location\n");
            } else if (!fromPlayer.hasTreasure(treasure)) {
                informPlayer(fromPlayerId, "You do not have this treasure\n");
            } else if (toPlayer.isBackpackFull()) {
                informPlayer(toPlayerId, "Your backpack is full");
                informPlayer(fromPlayerId, "Player " + toPlayerId + " backpack is full\n");
            } else {
                fromPlayer.removeTreasure(treasure);
                informPlayer(fromPlayerId, "You gave " + treasure.getTreasureName() + " \n");

                toPlayer.addTreasure(treasure);
                informPlayer(toPlayerId, "You received " + treasure.getTreasureName() + " from " + fromPlayerId + "  \n");
            }
        }
    }


    // THESE FUNCTIONS ARE RESPONSIBLE FOR SPAWNING A NEW PLAYER ON THE MAP
    public void acceptNewPlayer(Socket socketForPlayer, int playerId) {
        synchronized (mapChangeMonitor) {
            Position positionToStart = calculateBestPosition();
            Player player = new Player(playerId, positionToStart.getX(), positionToStart.getY());
            playerModelManager.addNewPlayerModel(playerId, player);
            playerConnectionManager.addNewPlayerConnection(playerId, socketForPlayer);
            changePlayerLocation(playerId, player, player.getXPos(), player.getYPos());
        }

        informPlayer(playerId, "Welcome to the server player: " + playerId + "\n");
        sendMapToAll();

        //TODO: Create new player
        //TODO: Assign the new player a location
        //TODO: Add the player in the player's manager for the world
        //TODO: Add the player's socket in the connection's manager for the world
        //TODO: Update the world to all players
    }

    //TODO: If currently there are no possible place to place the client, inform him
    //TODO: In the future add logic that will calculate location for the player on a position where no enemies are in the 4 directions
    //TODO: Make a logic so that the player is not blocked from all sides
    private Position calculateBestPosition() {
        for (int i = 0; i < MAX_ROWS; i++) {
            for (int j = 0; j < MAX_COLUMNS; j++) {
                if (worldMap[i][j].equals(EMPTY_SPACE)) {
                    return Position.of(i, j);
                }
            }
        }
        return Position.of(0,0);
    }

    // THESE FUNCTIONS are responsible to show this person's stats
    public void showPlayerStats(int playerID) {
        Player player = playerModelManager.getPlayer(playerID);
        informPlayer(playerID, player.toString());
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

    public void messagePlayer(int fromPlayerId, int toPlayeId, String message) {
        informPlayer(toPlayeId, message);
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
