package server.connection;

import model.world.World;
import server.command.CommandExecutor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class EstablishedPlayerConnection implements Runnable {

    private final CommandExecutor commandExecutor;
    private final Socket clientSocket;
    private boolean isConnectionActive;
    private final int playerId;

    public EstablishedPlayerConnection(CommandExecutor commandExecutor, Socket clientSocket, int playerId) {
        this.commandExecutor = commandExecutor;
        this.clientSocket = clientSocket;
        this.playerId = playerId;
        this.isConnectionActive = true;
    }

    @Override
    public void run() {
        try(BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            clientSocket)  {

            while(isConnectionActive) {
                String playerRequest;
                while((playerRequest = in.readLine()) != null) {
                    commandExecutor.executeRequest(playerRequest);
                }
            }

        } catch (IOException e) {
            System.out.println("Problems when communicating with the clinet");
            //TODO: Save this information in a file
        }
    }


}
