package model.treasure.weapon.player;

import model.treasure.TreasureFactory;
import model.treasure.weapon.Weapon;

public final class MasterSword extends Weapon {
    public static final int ATTACK_DMG = 7;
    public static final int LEVEL_REQ = 4;

    public MasterSword() {
        super(ATTACK_DMG, LEVEL_REQ, TreasureFactory.MASTER_SWORD);
    }
}
