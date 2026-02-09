package model.treasure.weapon.player;

import model.treasure.TreasureFactory;
import model.treasure.weapon.Weapon;

public class WoodenSword extends Weapon {
    private static final int ATTACK_DMG = 1;
    private static final int LEVEL_REQ = 1;

    public WoodenSword() {
        super(ATTACK_DMG, LEVEL_REQ, TreasureFactory.WOODEN_SWORD);
    }
}
