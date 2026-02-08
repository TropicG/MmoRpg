package model.character.player.storage;

import model.treasure.Treasure;

import java.util.ArrayList;
import java.util.List;

public class Backpack {
    private final List<Treasure> allTreasure;

    public Backpack() {
        allTreasure = new ArrayList<>();
    }

    public void addTreasure(Treasure treasure) {
        allTreasure.add(treasure);
    }

    public List<Treasure> getAllTreasure() {
        return allTreasure;
    }

    @Override
    public String toString() {
        StringBuilder allInventory = new StringBuilder();
        for(Treasure treasure : allTreasure) {
            allInventory.append(treasure.toString());
        }

        return allInventory.toString();
    }
}
