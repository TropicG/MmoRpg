package model.character.enemy;

public class Orc extends Minion {
    private static int ORC_HEALTH = 7;
    private static int ORC_MANA = 0;
    private static int ORC_DAMAGE = 4;
    private static int ORC_DEFENSE = 3;
    private static int ORC_LEVEL = 3;
    private static int ORC_XP_REWARD = 6;

    public Orc(int health, int mana, int damage, int defense, int currentLevel, int x, int y) {
        super(health, mana, damage, defense, currentLevel, x, y, ORC_XP_REWARD);
    }
}
