package model.world.manager;

import model.treasure.Treasure;
import model.treasure.weapon.Weapon;
import model.treasure.weapon.player.BattleAxe;
import model.treasure.weapon.player.CopperSword;
import model.treasure.weapon.player.MasterSword;
import model.treasure.weapon.player.WoodenSword;
import model.treasure.weapon.player.Xenoblade;

import java.util.Random;

public class TreasureManager {

    private static final int BATTLE_AXE = 0;
    private static final int COPPER_SWORD = 1;
    private static final int MASTER_SWORD = 2;
    private static final int WOODEN_SWORD = 3;
    private static final int XENOBLADE = 4;
    private static final int TOTAL_PLAYER_WEAPONS = 5;

    private static final int TREASURE_TYPE_WEAPON = 0;
    private static final int TREASURE_TYPE_SPELL = 1;
    private static final int TREASURE_TYPE_MANA_POTION = 2;
    private static final int TREASURE_TYPE_HEALTH_POTION = 3;
    private static final int TOTAL_TREASURE_TYPES = 4;


    private final Random random;

    public TreasureManager() {
        this.random = new Random();
    }

    public Weapon getRandomWeapon() {
        int weaponType = random.nextInt(TOTAL_PLAYER_WEAPONS);

        return switch (weaponType) {
            case BATTLE_AXE -> new BattleAxe();
            case COPPER_SWORD -> new CopperSword();
            case MASTER_SWORD -> new MasterSword();
            case WOODEN_SWORD -> new WoodenSword();
            case XENOBLADE -> new Xenoblade();
            default -> null;
        };
    }

    public Treasure getRandomTreasure() {
        return getRandomWeapon();
    }
}
