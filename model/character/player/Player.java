package model.character.player;

import model.character.Actor;
import model.character.player.storage.Backpack;
import model.treasure.Treasure;
import model.treasure.weapon.Weapon;

import java.util.List;

public class Player extends Actor {
    private static int PLAYER_STARTING_LEVEL = 1;
    private static int STARTING_PLAYER_HEALTH = 20;
    private static int STARTING_PLAYER_MANA = 10;
    private static int STARTING_PLAYER_DAMAGE = 5;
    private static int STARTING_PLAYER_DEFENSE = 3;

    public static int BACKPACK_CAPACITY = 10;

    //TODO: Generate a Builder pattern
    //TODO: Creating a Backpack
    //TODO: Creating a Container for Spells
    //TODO: Creating a Container for Potions

    private final int playerId;
    private final Backpack backpack;

    //TODO: Change the names of the parameters for the player location
    public Player(int playerId, int x, int y) {
        super(STARTING_PLAYER_HEALTH,
                STARTING_PLAYER_MANA,
                STARTING_PLAYER_DAMAGE,
                STARTING_PLAYER_DEFENSE,
                PLAYER_STARTING_LEVEL,
                x,y);

        this.playerId = playerId;
        this.backpack = new Backpack();
    }

    public void addTreasure(Treasure treasure) {
        backpack.addTreasure(treasure);
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
