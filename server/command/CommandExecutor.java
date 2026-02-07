package server.command;

import model.world.World;
import model.world.direction.Direction;

import java.io.IOException;

//TODO: This comamnd will be responsible for registering commands from the client
public class CommandExecutor {
    private World world;
    private final int playerId;

    public CommandExecutor(World world, int playerId) {
        this.world = world;
        this.playerId = playerId;
    }

    public World getWorld() {
        return world;
    }

    public void executeRequest(String request) throws IOException {
        String[] arguments = request.split(",");

        switch(arguments[0]) {
           case "up" -> world.movePlayerInDirection(playerId, Direction.UP);
           case "down" -> world.movePlayerInDirection(playerId, Direction.DOWN);
           case "left" -> world.movePlayerInDirection(playerId, Direction.LEFT);
           case "right" -> world.movePlayerInDirection(playerId, Direction.RIGHT);
        }
    }
}
