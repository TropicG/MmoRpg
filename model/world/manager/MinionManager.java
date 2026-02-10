package model.world.manager;

import model.character.enemy.Minion;
import model.character.position.Position;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MinionManager {
    private final Map<Position, Minion> allMinionsOnMap;

    public MinionManager() {
        allMinionsOnMap = new ConcurrentHashMap<>();
    }

    public void addMinionToWorld(Position position, Minion minion) {
        allMinionsOnMap.put(position, minion);
    }

    public Minion getMinion(Position minionPosition) {
        return allMinionsOnMap.get(minionPosition);
    }
}
