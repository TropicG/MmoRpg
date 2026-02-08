package model.treasure.weapon.player;

import model.treasure.weapon.Weapon;

public final class CopperSword extends Weapon {
    private static final int ATTACK_DMG = 3;
    private static final int LEVEL_REQ = 1;

    public CopperSword() {
        super(ATTACK_DMG, LEVEL_REQ, "CopperSword");
    }
}
