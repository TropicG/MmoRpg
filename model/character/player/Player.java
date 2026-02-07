package model.character.player;

import model.character.Actor;

public class Player extends Actor {
    public static int PLAYER_STARTING_LEVEL = 1;
    public static int STARTING_PLAYER_HEALTH = 20;
    public static int STARTING_PLAYER_MANA = 10;
    public static int STARTING_PLAYER_DAMAGE = 5;
    public static int STARTING_PLAYER_DEFENSE = 3;

    //TODO: Generate a Builder pattern
    //TODO: Creating a Backpack
    //TODO: Creating a Container for Spells
    //TODO: Creating a Container for Potions

    private final int playerId;

    //TODO: Change the names of the parameters for the player location
    public Player(int health, int mana, int damage, int defense, int currentLevel, int playerId, int x, int y) {
        super(health,mana,damage,defense,currentLevel, x, y);
        this.playerId = playerId;
    }
}
