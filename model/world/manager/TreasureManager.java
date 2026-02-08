package model.world.manager;

import model.treasure.Treasure;
import model.treasure.potion.AttackPotion;
import model.treasure.potion.DefensePotion;
import model.treasure.potion.HealthPotion;
import model.treasure.potion.ManaPotion;
import model.treasure.spell.Flamethrower;
import model.treasure.spell.HydroCannon;
import model.treasure.spell.Spell;
import model.treasure.spell.Thunderbolt;
import model.treasure.spell.Twister;
import model.treasure.weapon.Weapon;
import model.treasure.weapon.player.BattleAxe;
import model.treasure.weapon.player.CopperSword;
import model.treasure.weapon.player.MasterSword;
import model.treasure.weapon.player.WoodenSword;
import model.treasure.weapon.player.Xenoblade;

import java.util.Random;

public class TreasureManager {

    // all possible weapon types
    private static final int BATTLE_AXE = 0;
    private static final int COPPER_SWORD = 1;
    private static final int MASTER_SWORD = 2;
    private static final int WOODEN_SWORD = 3;
    private static final int XENOBLADE = 4;
    private static final int TOTAL_PLAYER_WEAPONS = 5;

    // all possible spell types
    private static final int FLAMETHROWER = 0;
    private static final int TWISTER = 1;
    private static final int THUNDERBOLT = 2;
    private static final int HYDRO_CANNON = 3;
    private static final int TOTAL_SPELLS = 4;

    // all possible potion types
    private static final int HEALTH_POTION = 0;
    private static final int MANA_POTION = 1;
    private static final int ATTACK_POTION = 2;
    private static final int DEFENSE_POTION = 3;
    private static final int ALL_SPELLS = 4;

    // all possible treasures types
    private static final int TREASURE_TYPE_WEAPON = 0;
    private static final int TREASURE_TYPE_SPELL = 1;
    private static final int TREASURE_TYPE_POTION = 2;
    private static final int TOTAL_TREASURE_TYPES = 3;


    private final Random random;

    public TreasureManager() {
        this.random = new Random();
    }

    public Treasure getRandomTreasure() {
        int treasureType = random.nextInt(TOTAL_TREASURE_TYPES);
        return switch (treasureType) {
            case TREASURE_TYPE_WEAPON -> getRandomWeapon();
            case TREASURE_TYPE_SPELL -> getRandomSpell();
            case TREASURE_TYPE_POTION -> getRandomPotion();
            default -> null;
        };
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

    public Spell getRandomSpell() {
        int spellType = random.nextInt(TOTAL_SPELLS);

        return switch (spellType) {
            case FLAMETHROWER -> new Flamethrower();
            case TWISTER -> new Twister();
            case THUNDERBOLT -> new Thunderbolt();
            case HYDRO_CANNON -> new HydroCannon();
            default -> null;
        };
    }


    public Treasure getRandomPotion() {
        int potionType = random.nextInt(ALL_SPELLS);

        return switch (potionType) {
            case HEALTH_POTION -> new HealthPotion();
            case MANA_POTION -> new ManaPotion();
            case ATTACK_POTION -> new AttackPotion();
            case DEFENSE_POTION -> new DefensePotion();
            default -> null;
        };
    }
}
