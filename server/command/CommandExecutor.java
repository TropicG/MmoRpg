package server.command;

import model.world.World;
import model.world.direction.Direction;

import java.io.IOException;

//TODO: Create command executor as a SingleTon
//TODO: This comamnd will be responsible for registering commands from the client
public class CommandExecutor {
    // argument placement
    private static int COMMAND_ARG = 0;
    private static int OTHER_PLAYER_ID_ARG = 1;

    // formatting player's request
    private static int REMOVE_QUOTE = 1;
    private static String ARG_SEPERATOR = " ";

    // commands
    private static final String upCommand = "up";
    private static final String downCommand = "down";
    private static final String leftCommand = "left";
    private static final String rightCommand = "right";
    private static final String msgCommand = "msg";
    private static final String invCommand = "inv";
    private static final String statsCommand = "stats";

    private static final World world = World.getInstance();

    public static void executeRequest(int playerId,String request) throws IOException {
        String[] arguments = request.split(ARG_SEPERATOR);

        switch(arguments[COMMAND_ARG]) {
           case upCommand -> world.movePlayerInDirection(playerId, Direction.UP);
           case downCommand -> world.movePlayerInDirection(playerId, Direction.DOWN);
           case leftCommand -> world.movePlayerInDirection(playerId, Direction.LEFT);
           case rightCommand -> world.movePlayerInDirection(playerId, Direction.RIGHT);
           case msgCommand -> messagePlayer(playerId, request, arguments);
           case invCommand -> world.showInventoryToPlayer(playerId);
            case statsCommand -> world.showPlayerStats(playerId);
        }
    }

    private static void messagePlayer(int playerId, String request, String[] arguments) {
        String actualMessage = "Message from " + playerId + ": " + getQuotedMessage(request) + "\n";
        world.messagePlayer(playerId, Integer.parseInt(arguments[OTHER_PLAYER_ID_ARG]), actualMessage );
    }

    private static String getQuotedMessage(String request) {
        return request.substring(request.indexOf("\"") + REMOVE_QUOTE, request.lastIndexOf("\""));
    }
}
