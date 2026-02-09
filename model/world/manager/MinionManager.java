package model.world.manager;

import model.character.enemy.Minion;
import model.character.position.Position;

import java.util.HashMap;
import java.util.Map;

public class MinionManager {
    private final Map<Position, Minion> allMinionsOnMap;

    public MinionManager() {
        allMinionsOnMap = new HashMap<>();
    }

    public void addMinionToWorld(Position position, Minion minion) {
        allMinionsOnMap.put(position, minion);
    }

    public Minion getMinion(Position minionPosition) {
        return allMinionsOnMap.get(minionPosition);
    }
}
