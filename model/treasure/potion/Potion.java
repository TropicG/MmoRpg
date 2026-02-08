package model.treasure.potion;

import model.treasure.Treasure;

public abstract class Potion extends Treasure {
    protected Potion(int potionPoints, String name) {
        super(potionPoints, name);
    }
}
