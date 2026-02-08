package server.connection;

import server.command.CommandExecutor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class EstablishedPlayerConnection implements Runnable {

    private final Socket clientSocket;
    private boolean isConnectionActive;
    private final int playerId;

    public EstablishedPlayerConnection(Socket clientSocket, int playerId) {
        this.clientSocket = clientSocket;
        this.playerId = playerId;
        this.isConnectionActive = true;
    }

    @Override
    public void run() {
        try(BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            clientSocket)  {

            while(isConnectionActive) {
                String playerRequest;
                while((playerRequest = in.readLine()) != null) {
                    CommandExecutor.executeRequest(playerId,playerRequest);
                }
            }

        } catch (IOException e) {
            System.out.println("Problems when communicating with the clinet");
            System.out.println(e.getMessage());
            //TODO: Save this information in a file
        }
    }
}
