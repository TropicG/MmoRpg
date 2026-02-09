package model.treasure;

import model.treasure.potion.AttackPotion;
import model.treasure.potion.DefensePotion;
import model.treasure.potion.HealthPotion;
import model.treasure.potion.ManaPotion;
import model.treasure.potion.Potion;
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

public class TreasureFactory {
    // names of all the items
    public static final String ATTACK_POTION = "AttackPotion";
    public static final String DEFENSE_POTION = "DefensePotion";
    public static final String HEALTH_POTION = "HealthPotion";
    public static final String MANA_POTION = "ManaPotion";
    public static final String FLAMETHROWER = "Flamethrower";
    public static final String HYDRO_CANNON = "HydroCannon";
    public static final String THUNDERBOLT = "Thunderbolt";
    public static final String TWISTER = "Twister";
    public static final String BATTLE_AXE = "BattleAxe";
    public static final String COPPER_SWORD = "CopperSword";
    public static final String MASTER_SWORD = "MasterSword";
    public static final String WOODEN_SWORD = "WoodenSword";
    public static final String XENOBLADE = "Xenoblade";

    public static Treasure of(String treasureName) {

        // if the treasure is weapon
        Weapon weapon = ofWeapon(treasureName);
        if(weapon != null) {
            return weapon;
        }

        // if the treasureName is spell
        Spell spell = ofSpell(treasureName);
        if(spell != null) {
            return spell;
        }

        // if the treasureName is potion
        Potion potion = ofPotion(treasureName);
        if(potion != null) {
            return potion;
        }

        // return null if neither
        return null;
    }

    public static Weapon ofWeapon(String weaponName) {
        return switch (weaponName) {
            case BATTLE_AXE -> new BattleAxe();
            case COPPER_SWORD -> new CopperSword();
            case MASTER_SWORD -> new MasterSword();
            case WOODEN_SWORD -> new WoodenSword();
            case XENOBLADE -> new Xenoblade();
            default -> null;
        };
    }

    public static Spell ofSpell(String spellName) {
        return switch (spellName) {
            case FLAMETHROWER -> new Flamethrower();
            case HYDRO_CANNON -> new HydroCannon();
            case THUNDERBOLT -> new Thunderbolt();
            case TWISTER -> new Twister();
            default -> null;
        };
    }

    public static Potion ofPotion(String potionName) {
        return switch (potionName) {
            case ATTACK_POTION -> new AttackPotion();
            case DEFENSE_POTION -> new DefensePotion();
            case HEALTH_POTION -> new HealthPotion();
            case MANA_POTION -> new ManaPotion();
            default -> null;
        };
    }

}
