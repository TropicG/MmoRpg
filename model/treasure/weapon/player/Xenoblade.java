package model.treasure.weapon.player;

import model.treasure.TreasureFactory;
import model.treasure.weapon.Weapon;

public final class Xenoblade extends Weapon {
    public static final int ATTACK_DMG = 8;
    public static final int LEVEL_REQ = 5;

    public Xenoblade() {
        super(ATTACK_DMG, LEVEL_REQ, TreasureFactory.XENOBLADE);
    }
}
