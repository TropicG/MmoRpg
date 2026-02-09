package model.character.enemy;

import java.util.Random;

public class MinionFactory {
    private static int ORC_HEALTH = 7;
    private static int ORC_MANA = 0;
    private static int ORC_DAMAGE = 4;
    private static int ORC_DEFENSE = 3;
    private static int ORC_XP_REWARD = 6;

    private static int MIN_MINION_LEVEL = 1;
    private static int MAX_MINION_LEVEL = 10;

    public static Random random = new Random();

    public static Minion ofOrc(int xPos, int yPos) {
        int randomLevel = random.nextInt(MIN_MINION_LEVEL, MAX_MINION_LEVEL + 1);

        return new Orc(ORC_HEALTH, ORC_MANA, ORC_DAMAGE, ORC_DEFENSE, randomLevel, xPos, yPos, ORC_XP_REWARD);
    }

}
