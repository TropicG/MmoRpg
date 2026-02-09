package model.treasure.weapon.player;

import model.treasure.TreasureFactory;
import model.treasure.weapon.Weapon;

public final class BattleAxe extends Weapon {
    private static final int ATTACK_DMG = 4;
    private static final int LEVEL_REQ = 2;

    public BattleAxe() {
        super(ATTACK_DMG, LEVEL_REQ, TreasureFactory.BATTLE_AXE);
    }
}
