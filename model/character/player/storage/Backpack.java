package model.character.player.storage;

import model.treasure.Treasure;

import java.util.ArrayList;
import java.util.List;

public class Backpack {
    public static int BACKPACK_CAPACITY = 2;

    private final List<Treasure> allTreasure;

    public Backpack() {
        allTreasure = new ArrayList<>(BACKPACK_CAPACITY);
    }

    public void addTreasure(Treasure treasure) {
        allTreasure.add(treasure);
    }

    public void removeTreasure(Treasure treasure) {
        allTreasure.remove(treasure);
    }

    public List<Treasure> getAllTreasure() {
        return allTreasure;
    }

    public boolean hasTreasure(Treasure treasure) {
        for(Treasure backpackTreasure : allTreasure) {
            if(treasure.equals(backpackTreasure)) {
                return true;
            }
        }

        return false;
    }

    public boolean isBackpackFull() {
        return allTreasure.size() == BACKPACK_CAPACITY;
    }

    public boolean isBackpackEmpty() {
        return allTreasure.isEmpty();
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
