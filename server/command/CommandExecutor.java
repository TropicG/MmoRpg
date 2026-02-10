package server.command;

import model.treasure.Treasure;
import model.treasure.TreasureFactory;
import model.treasure.potion.Potion;
import model.treasure.spell.Spell;
import model.treasure.weapon.Weapon;
import model.world.World;
import model.world.direction.Direction;

import java.io.IOException;

public class CommandExecutor {
    // argument placement
    private static final int COMMAND_ARG = 0;
    private static final int TREASURE_NAME = 1;
    private static final int OTHER_PLAYER_ID_ARG = 1;
    private static final int TARGET_NAME_ARG = 1;
    private static final int TREASURE_NAME_ARG = 2;

    // formatting player's request
    private static int REMOVE_QUOTE = 1;
    private static String ARG_SEPERATOR = " ";

    // commands
    private static final String upCommand = "up";
    private static final String downCommand = "down";
    private static final String leftCommand = "left";
    private static final String rightCommand = "right";
    private static final String msgCommand = "msg";
    private static final String invCommand = "inv";
    private static final String statsCommand = "stats";
    private static final String giveCommand = "give";
    private static final String dropCommand = "drop";
    private static final String equipCommand = "eq";
    private static final String attackCommand = "attack";
    private static final String drinkCommand = "drink";
    private static final String castCommand = "cast";

    private static final World world = World.getInstance();

    public static void executeRequest(int playerId, String request) throws IOException {
        String[] arguments = request.split(ARG_SEPERATOR);

        switch (arguments[COMMAND_ARG]) {
            case upCommand -> world.movePlayerInDirection(playerId, Direction.UP);
            case downCommand -> world.movePlayerInDirection(playerId, Direction.DOWN);
            case leftCommand -> world.movePlayerInDirection(playerId, Direction.LEFT);
            case rightCommand -> world.movePlayerInDirection(playerId, Direction.RIGHT);
            case msgCommand -> messagePlayer(playerId, request, arguments);
            case invCommand -> world.showInventoryToPlayer(playerId);
            case statsCommand -> world.showPlayerStats(playerId);
            case giveCommand -> giveItemToPlayer(playerId, arguments);
            case dropCommand -> dropTreasureFromPlayer(playerId, arguments[TREASURE_NAME]);
            case equipCommand -> equipWeaponToPlayer(playerId, arguments[TREASURE_NAME]);
            case attackCommand -> executeAttack(playerId, arguments[TARGET_NAME_ARG]);
            case drinkCommand -> drinkPotion(playerId, arguments[TREASURE_NAME]);
            case castCommand -> castSpell(playerId, arguments[TARGET_NAME_ARG], arguments[TREASURE_NAME_ARG]);
        }
    }

    private static void drinkPotion(int playerId, String potionName) {
        Potion potion = TreasureFactory.ofPotion(potionName);
        world.playerDrinkPotion(playerId, potion);
    }

    private static void executeAttack(int playerId, String targetName) {
        if(targetName.equals(World.MINION_SPACE)) {
            world.attackMinion(playerId);
        } else {
            int toPlayer = Integer.parseInt(targetName);
            world.attackPlayer(playerId, toPlayer);
        }
    }

    private static void castSpell(int playerId, String targetName, String spellName) {
        Spell spell = TreasureFactory.ofSpell(spellName);

        if(targetName.equals(World.MINION_SPACE)) {
            world.castSpellMinion(playerId, spell);
        }
    }

    private static void equipWeaponToPlayer(int playerId, String weaponName) {
        Weapon weapon = TreasureFactory.ofWeapon(weaponName);
        world.equipWeaponToPlayer(playerId, weapon);
    }

    private static void dropTreasureFromPlayer(int playerId, String treasureName){
        Treasure treasure = TreasureFactory.of(treasureName);
        world.dropTreasureFromPlayer(playerId, treasure);
    }

    private static void giveItemToPlayer(int playerId, String[] arguments) {
        Treasure treasure = TreasureFactory.of(arguments[TREASURE_NAME_ARG]);
        int toPlayer = Integer.parseInt(arguments[OTHER_PLAYER_ID_ARG]);

        world.giveTreasureToPlayer(playerId, toPlayer, treasure);
    }

    private static void messagePlayer(int playerId, String request, String[] arguments) {
        String actualMessage = "Message from " + playerId + ": " + getQuotedMessage(request) + "\n";
        int toPlayer = Integer.parseInt(arguments[OTHER_PLAYER_ID_ARG]);

        world.messagePlayer(playerId, toPlayer, actualMessage);
    }

    private static String getQuotedMessage(String request) {
        return request.substring(request.indexOf("\"") + REMOVE_QUOTE, request.lastIndexOf("\""));
    }

}
