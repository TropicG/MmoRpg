package server.command;

import model.world.World;
import model.world.direction.Direction;

import java.io.IOException;

//TODO:
//TODO: This comamnd will be responsible for registering commands from the client
public class CommandExecutor {

    private static int COMMAND_ARG = 0;
    private static int OTHER_PLAYER_ID_ARG = 1;

    private static int REMOVE_QUOTE = 1;

    private World world;
    private final int playerId;

    public CommandExecutor(World world, int playerId) {
        this.world = world;
        this.playerId = playerId;
    }

    public void executeRequest(String request) throws IOException {
        String[] arguments = request.split(" ");

        switch(arguments[COMMAND_ARG]) {
           case "up" -> world.movePlayerInDirection(playerId, Direction.UP);
           case "down" -> world.movePlayerInDirection(playerId, Direction.DOWN);
           case "left" -> world.movePlayerInDirection(playerId, Direction.LEFT);
           case "right" -> world.movePlayerInDirection(playerId, Direction.RIGHT);
           case "msg" -> messagePlayer(request, arguments);
        }
    }

    private void messagePlayer(String request, String[] arguments) {
        String actualMessage = "Message from " + playerId + ": " + getQuotedMessage(request) + "\n";
        world.messagePlayer(playerId, Integer.parseInt(arguments[OTHER_PLAYER_ID_ARG]), actualMessage );
    }

    private String getQuotedMessage(String request) {
        return request.substring(request.indexOf("\"") + REMOVE_QUOTE, request.lastIndexOf("\""));
    }
}
