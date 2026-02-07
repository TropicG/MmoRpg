package model.world.manager;

import model.character.player.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerModelManager {
    private final Map<Integer, Player> activePlayersModels;

    public PlayerModelManager() {
        this.activePlayersModels = new HashMap<>();
    }

    public void addNewPlayerModel(Integer id, Player newPlayer) {
        activePlayersModels.put(id,newPlayer);
    }

    public Player getPlayer(Integer id) {
        return activePlayersModels.get(id);
    }

    public void updatePlayerPosition(Integer id, int newPosX, int newPosY) {
        activePlayersModels.get(id).setXpos(newPosX);
        activePlayersModels.get(id).setYpos(newPosY);
    }
}
