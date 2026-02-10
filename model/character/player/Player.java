package model.character.player;

import model.character.Actor;
import model.character.player.storage.Backpack;
import model.treasure.Treasure;
import model.treasure.TreasureFactory;
import model.treasure.potion.AttackPotion;
import model.treasure.potion.DefensePotion;
import model.treasure.potion.HealthPotion;
import model.treasure.potion.ManaPotion;
import model.treasure.potion.Potion;
import model.treasure.spell.Spell;
import model.treasure.weapon.Weapon;

import java.util.Random;

public class Player extends Actor {
    // initial stats
    public static final int PLAYER_STARTING_LEVEL = 1;
    public static final int STARTING_PLAYER_HEALTH = 100;
    public static final int STARTING_PLAYER_MANA = 100;
    public static final int STARTING_PLAYER_DAMAGE = 50;
    public static final int STARTING_PLAYER_DEFENSE = 30;
    public static final int REWARD_XP = 25;

    public static final int INITIAL_XP_BAR = 25;

    // stats for levelUp
    public static final int HEALTH_LEVEL_UP = 10;
    public static final int MANA_LEVEL_UP = 10;
    public static final int ATTACK_LEVEL_UP = 5;
    public static final int DEFENSE_LEVEL_UP = 5;
    public static final int XP_INCREASE = 50;

    private final Backpack backpack;
    private Weapon equipedWeapon;

    private int xpBar;
    private int neededXp;

    private Random random;

    public Player(int playerId, int x, int y) {
        super(STARTING_PLAYER_HEALTH,
                STARTING_PLAYER_MANA,
                STARTING_PLAYER_DAMAGE,
                STARTING_PLAYER_DEFENSE,
                PLAYER_STARTING_LEVEL,
                x, y, REWARD_XP);

        this.backpack = new Backpack();
        equipedWeapon = null;

        xpBar = 0;
        neededXp = INITIAL_XP_BAR;

        random = new Random();
    }

    @Override
    public int calculateAtkDmg() {
        int totalAttackDmg = super.damage;

        if (equipedWeapon != null) {
            totalAttackDmg += equipedWeapon.getAttackDmg();
        }

        return totalAttackDmg;
    }

    public void addXp(int xpReward) {
        xpBar += xpReward;
        if (xpBar >= neededXp) {
            levelUp();
        }
    }

    public boolean hasLeveledUp() {
        return (xpBar == 0) && (currentLevel != PLAYER_STARTING_LEVEL);
    }

    public void levelUp() {
        totalHealth = totalHealth + HEALTH_LEVEL_UP;
        totalMana = totalMana + MANA_LEVEL_UP;
        damage += ATTACK_LEVEL_UP;
        defense += DEFENSE_LEVEL_UP;

        currentLevel++;

        xpBar = 0;
        neededXp += XP_INCREASE;
    }

    public void drinkPotion(Potion potion) {
        switch (potion.getTreasureName()) {
            case TreasureFactory.ATTACK_POTION -> super.damage += AttackPotion.ATTACK_POINTS;
            case TreasureFactory.HEALTH_POTION -> {
                currentHealth += HealthPotion.HEALTH_POINTS;
                if (currentHealth > totalHealth) {
                    currentHealth = totalHealth;
                }
            }
            case TreasureFactory.MANA_POTION -> {
                currentMana += ManaPotion.MANA_POINTS;
                if (currentMana >= totalMana) {
                    currentMana = totalMana;
                }
            }
            case TreasureFactory.DEFENSE_POTION -> super.defense += DefensePotion.DEFENSE_POINTS;
        }
    }

    public boolean canPerformSpell(Spell spell) {
        return currentMana >= spell.getManaCost() && currentLevel >= spell.getLevelReq();
    }

    public void castSpell(Spell spell) {
        currentMana -= spell.getManaCost();
        if (currentMana < 0) {
            currentMana = 0;
        }
    }

    public String removeRandomTreasure() {
        // select item
        int randomItem = random.nextInt(0, backpack.getItemCountBackpack());
        Treasure selectedTreasure = backpack.getAllTreasure().get(randomItem);

        // if the selected item is indeed the equipped weapon, we remove it
        if (selectedTreasure.equals(equipedWeapon)) {
            removeWeapon();
        }

        removeTreasure(selectedTreasure);

        return selectedTreasure.getTreasureName();
    }

    public void revive() {
        totalHealth = STARTING_PLAYER_HEALTH;
        currentHealth = totalHealth;

        totalMana = STARTING_PLAYER_MANA;
        currentMana = totalMana;

        damage = STARTING_PLAYER_DAMAGE;
        defense = STARTING_PLAYER_DEFENSE;
        currentLevel = PLAYER_STARTING_LEVEL;

        xpBar = 0;
        neededXp = INITIAL_XP_BAR;

    }

    public void addTreasure(Treasure treasure) {
        backpack.addTreasure(treasure);
    }

    public void removeTreasure(Treasure treasure) {
        backpack.removeTreasure(treasure);
    }

    public boolean hasTreasure(Treasure treasure) {
        return backpack.hasTreasure(treasure);
    }

    public boolean isBackpackFull() {
        return backpack.isBackpackFull();
    }

    public boolean isBackpackEmpty() {
        return backpack.isBackpackEmpty();
    }

    public Weapon getEquipedWeapon() {
        return equipedWeapon;
    }

    public boolean equipWeapon(Weapon weapon) {
        if(currentLevel < weapon.getLevelReq()) {
            return false;
        }

        this.equipedWeapon = weapon;
        return true;
    }

    public void removeWeapon() {
        this.equipedWeapon = null;
    }

    public Backpack getBackpack() {
        return backpack;
    }

    public int getXpNeeded() {
        return neededXp;
    }

    public int getXp() {
        return xpBar;
    }

    @Override
    public String toString() {
        StringBuilder playerStats = new StringBuilder();

        playerStats.append("HP: ").append(super.getCurrentHealth()).append("/").append(super.getTotalHealth()).append(", ");
        playerStats.append("Mana: ").append(super.getCurrentMana()).append("/").append(super.getTotalMana()).append(", ");
        playerStats.append("Dmg: ").append(super.getDamage()).append(", ");
        playerStats.append("Def: ").append(super.getDefense()).append(", ");
        playerStats.append("Lvl: ").append(super.getCurrentLevel()).append(", ");
        playerStats.append("Xp: ").append(xpBar).append("/").append(neededXp);

        if (getEquipedWeapon() != null) {
            playerStats.append(", Weapon: ").append(getEquipedWeapon().toString());
        }

        playerStats.append("\n");

        return playerStats.toString();
    }
}
