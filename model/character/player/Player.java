package model.character.player;

import model.character.Actor;
import model.character.player.storage.Backpack;
import model.treasure.Treasure;
import model.treasure.weapon.Weapon;

public class Player extends Actor {
    private static final int PLAYER_STARTING_LEVEL = 1;
    private static final int STARTING_PLAYER_HEALTH = 20;
    private static final int STARTING_PLAYER_MANA = 10;
    private static final int STARTING_PLAYER_DAMAGE = 5;
    private static final int STARTING_PLAYER_DEFENSE = 3;
    private static final int REWARD_XP = 10;

    //TODO: Generate a Builder pattern
    //TODO: Creating a Backpack
    //TODO: Creating a Container for Spells
    //TODO: Creating a Container for Potions

    private final Backpack backpack;
    private Weapon equipedWeapon;

    //TODO: Change the names of the parameters for the player location
    public Player(int playerId, int x, int y) {
        super(STARTING_PLAYER_HEALTH,
                STARTING_PLAYER_MANA,
                STARTING_PLAYER_DAMAGE,
                STARTING_PLAYER_DEFENSE,
                PLAYER_STARTING_LEVEL,
                x,y, REWARD_XP);

        this.backpack = new Backpack();
        equipedWeapon = null;
    }

    @Override
    public int calculateAtkDmg() {
        int totalAttackDmg = super.damage;

        if(equipedWeapon != null) {
            totalAttackDmg += equipedWeapon.getAttackDmg();
        }

        return totalAttackDmg;
    }

    public void addTreasure(Treasure treasure) {
        backpack.addTreasure(treasure);
    }

    public void removeTreasure(Treasure treasure){
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

    public void equipWeapon(Weapon weapon) {
        this.equipedWeapon = weapon;
    }

    public Backpack getBackpack() {
        return backpack;
    }

    @Override
    public String toString() {
        StringBuilder playerStats = new StringBuilder();

        playerStats.append("HP: ").append(super.getHealth()).append(", Mana: ").append(super.getMana()).append(", ");
        playerStats.append("Dmg: ").append(super.getDamage()).append(", Def: ")
                .append(super.getDefense()).append(", Lvl: ").append(super.getCurrentLevel());
        playerStats.append("\n");

        return playerStats.toString();
    }
}
