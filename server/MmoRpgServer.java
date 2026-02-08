package server;

import model.world.World;
import server.command.CommandExecutor;
import server.connection.EstablishedPlayerConnection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MmoRpgServer {
    private static int SERVER_PORT = 8080;
    private static boolean isServerRuning = true;
    private static int MAX_ACTIVE_CLIENTS = 9;

    private static final World world = World.getInstance();
    private static int connectedPlayers = 0;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
             ExecutorService executor = Executors.newFixedThreadPool(MAX_ACTIVE_CLIENTS)) {

            while (isServerRuning) {
                listenForNewPlayers(serverSocket, executor);
            }

        } catch (IOException ioException) {
            System.out.println("PROBLEMS WITH THE SERVER");
            //TODO: Save to this current computer why there are problems with the server
        }
    }

    private static void listenForNewPlayers(ServerSocket serverSocket, ExecutorService executor) {
        Socket playerSocket;
        try {
            playerSocket = serverSocket.accept();
            if (playerSocket != null) {
                connectedPlayers++;
                runThreadForPlayConnection(playerSocket, executor);
                worldAcceptNewPlayer(playerSocket);
            }
        } catch (IOException e) {
            System.out.println("PROBLEMS WHEN ACCEPTING A PLAYER IN THE SERVER");
            //TODO: Inform the player with log files that something happened to his computer
        }
    }

    private static void worldAcceptNewPlayer(Socket playerSocket) throws IOException {
        world.acceptNewPlayer(playerSocket, connectedPlayers);
    }

    private static void runThreadForPlayConnection(Socket playerSocket, ExecutorService executor) {
        CommandExecutor commandExecutor = new CommandExecutor(world, connectedPlayers);
        EstablishedPlayerConnection playerConnection = new EstablishedPlayerConnection(commandExecutor, playerSocket, connectedPlayers);
        executor.execute(playerConnection);
    }
}
