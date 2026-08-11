package model.treasure.weapon.player;

import model.treasure.TreasureFactory;
import model.treasure.weapon.Weapon;

public class WoodenSword extends Weapon {
    public static final int ATTACK_DMG = 1;
    public static final int LEVEL_REQ = 1;

    public WoodenSword() {
        super(ATTACK_DMG, LEVEL_REQ, TreasureFactory.WOODEN_SWORD);
    }
}
