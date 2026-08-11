import jdk.jfr.Name;
import model.character.player.Player;
import model.treasure.TreasureFactory;
import model.treasure.weapon.Weapon;
import model.treasure.weapon.player.BattleAxe;
import model.treasure.weapon.player.CopperSword;
import model.treasure.weapon.player.MasterSword;
import model.treasure.weapon.player.WoodenSword;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlayerTest {

    private Player player;

    private final int PLAYER_ID = 1;

    private final int STARTING_POSITION_X = 0;
    private final int STARTING_POSITION_Y = 0;

    @BeforeEach
    public void setUp() {
        player = new Player(PLAYER_ID, STARTING_POSITION_X, STARTING_POSITION_Y );
    }

    @Test
    public void testPlayerInitialStatsUponRespawn() {
        // testing the starting current and total health of the player
        assertEquals(Player.STARTING_PLAYER_HEALTH, player.getCurrentHealth(),
                "The player's current should have " + Player.STARTING_PLAYER_HEALTH);
        assertEquals(Player.STARTING_PLAYER_HEALTH, player.getTotalHealth(),
                "The player's health should have " + Player.STARTING_PLAYER_HEALTH);

        // testing the starting current mana and total mana of the player
        assertEquals(Player.STARTING_PLAYER_MANA, player.getCurrentMana(),
                "The player's mana should have " + Player.STARTING_PLAYER_MANA);
        assertEquals(Player.STARTING_PLAYER_HEALTH, player.getCurrentHealth(),
                "The player should have " + Player.STARTING_PLAYER_HEALTH);

        // testing the starting damage and defense of the player
        assertEquals(Player.STARTING_PLAYER_DAMAGE, player.getDamage(),
                "The player's damage should be " + Player.STARTING_PLAYER_DAMAGE);
        assertEquals(Player.STARTING_PLAYER_DEFENSE, player.getDefense(),
                "The player's defense should be " + Player.STARTING_PLAYER_DEFENSE);

        // testing player current X and Y for the player
        assertEquals(player.getXPos(), STARTING_POSITION_X,
                "The player's current X position should be " + STARTING_POSITION_X );
        assertEquals(player.getYPos(), STARTING_POSITION_Y,
                "The player's current Y position should be " + STARTING_POSITION_Y);

        // testing current player's level
        assertEquals(Player.PLAYER_STARTING_LEVEL, player.getCurrentLevel(),
                "The starting player's level should be " + Player.PLAYER_STARTING_LEVEL);

        // the player should not have equiped weapon upon creation
        assertNull(player.getEquipedWeapon(), "The current player should not have equiped a weapon");

        // the backpack of newly created player shall be empty
        assertEquals(0, player.getBackpack().getItemCountBackpack(),
                "The player's backpack shall be empty (0) when he is created");

        // testing current xpNeeded
        assertEquals(0, player.getXp(),
                "The player should have 0 xp at the very start");
        assertEquals(Player.INITIAL_XP_BAR, player.getXpNeeded(),
                "The player needs " + Player.INITIAL_XP_BAR + " xp to finish first level");

        // testing attackDmg of the new player
        assertEquals(Player.STARTING_PLAYER_DAMAGE, player.calculateAtkDmg(),
                "Upon creation the attackDmg of the player shall be " + Player.STARTING_PLAYER_DAMAGE);
    }

    // Test regarding equiping a BattleAxe
    @Test
    @Name("Player cannot equip BattleAxe if he doesn't meet required lvl")
    public void testEquipWeaponBattleAxeNotSuccessfulPlayerLvlLow() {
        // spawning battleAxe for the player to equip
        Weapon battleAxe = (Weapon) TreasureFactory.of("BattleAxe");

        // testing does battleAxe has proper attackDmg and lvl required
        assertEquals(BattleAxe.ATTACK_DMG, battleAxe.getAttackDmg(),
                "The battleaxe's attack dmg shall be " + BattleAxe.ATTACK_DMG);
        assertEquals(BattleAxe.LEVEL_REQ, battleAxe.getLevelReq(),
                "The battleaxe's lvl required shall be " + BattleAxe.LEVEL_REQ);

        // the player doesnt not have the required level
        assertEquals(player.getCurrentLevel(), Player.PLAYER_STARTING_LEVEL,
                "The player level shall be " + Player.PLAYER_STARTING_LEVEL);
        assertFalse(player.equipWeapon(battleAxe),
                "The battleAxe cannot be equipped since the required level for it is " + BattleAxe.LEVEL_REQ
        + " and the player level is " + Player.PLAYER_STARTING_LEVEL);
    }

    @Test
    @Name("Player can equip BattleAxe if he meets the required lvl")
    public void testEquipWeaponBattleAxeSuccessfulPlayerLvlEnough() {
        // spawning battleAxe for the player to equip
        Weapon battleAxe = (Weapon) TreasureFactory.of("BattleAxe");

        // testing does battleAxe has proper attackDmg and lvl required
        assertEquals(BattleAxe.ATTACK_DMG, battleAxe.getAttackDmg(),
                "The battleaxe's attack dmg shall be " + BattleAxe.ATTACK_DMG);
        assertEquals(BattleAxe.LEVEL_REQ, battleAxe.getLevelReq(),
                "The battleaxe's lvl required shall be " + BattleAxe.LEVEL_REQ);

        // level up the player enough so he can equip the battleAxe
        int levelUpWith = 1;
        for(int i = 0; i < levelUpWith; i++) {
            player.levelUp();
        }

        // check player level, if he has leveled up enought
        assertEquals(Player.PLAYER_STARTING_LEVEL + levelUpWith, player.getCurrentLevel(),
                "The player level shall be " + Player.PLAYER_STARTING_LEVEL + levelUpWith);

        // chekc if the player has equiped the level
        assertTrue(player.equipWeapon(battleAxe),
                "The player can equip the BattleAxe since the BattleAxe level is " +
                BattleAxe.LEVEL_REQ + " and the player level is " + player.getCurrentLevel());

        // check is the attackDmg has increased since the equipment of the weapon
        int expectedDamage = Player.STARTING_PLAYER_DAMAGE + BattleAxe.ATTACK_DMG + Player.ATTACK_LEVEL_UP;
        assertEquals(expectedDamage, player.calculateAtkDmg(),
                "The total damage of the player shall be " + expectedDamage +
                " but it is " + player.calculateAtkDmg());
    }

    // Test regarding equiping a CooperSword
    @Test
    @Name("Player can equip CopperSword")
    public void testEquipWeaponCopperSwordSuccessfulPlayerLvlEnough() {
        // spawning copperSword for the player to equip
        Weapon copperSword = (Weapon) TreasureFactory.of("CopperSword");

        // testing does copperSword has proper attackDmg and lvl required
        assertEquals(CopperSword.ATTACK_DMG, copperSword.getAttackDmg(),
                "The copper sword's attack dmg shall be " + CopperSword.ATTACK_DMG);
        assertEquals(CopperSword.LEVEL_REQ, copperSword.getLevelReq(),
                "The copper's lvl required shall be " + CopperSword.LEVEL_REQ);

        // checks if the player can equip the copperSword
        assertTrue(player.equipWeapon(copperSword),
                "The player can equip the CopperSword since the CopperSword level is " +
                BattleAxe.LEVEL_REQ + " and the player level is " + player.getCurrentLevel());

        int expectedDamage = Player.STARTING_PLAYER_DAMAGE + CopperSword.ATTACK_DMG;
        assertEquals(expectedDamage, player.calculateAtkDmg(),
                "The total damage of the player shall be " + expectedDamage +
                        " but it is " + player.calculateAtkDmg());
    }

    // Test regarding equpping a WoodenSword
    @Test
    @Name("Player can equip WoodenSword")
    public void testEquipWeaponWoodenSwordSuccessfulPlayerLvlEnough() {
        // spawning woodenSword for the player to equip
        Weapon woodenSword = (Weapon) TreasureFactory.of("WoodenSword");

        // testing does woodenSword has proper attackDmg and lvl required
        assertEquals(WoodenSword.ATTACK_DMG, woodenSword.getAttackDmg(),
                "The wooden sword's attack dmg shall be " + WoodenSword.ATTACK_DMG);
        assertEquals(WoodenSword.LEVEL_REQ, woodenSword.getLevelReq(),
                "The wooden's lvl required shall be " + WoodenSword.LEVEL_REQ);

        // checks if the player can equip the woodenSword
        assertTrue(player.equipWeapon(woodenSword),
                "The player can equip the WoodenSword since the WoodenSword level is " +
                        WoodenSword.LEVEL_REQ + " and the player level is " + player.getCurrentLevel());

        int expectedDamage = Player.STARTING_PLAYER_DAMAGE + WoodenSword.ATTACK_DMG;
        assertEquals(expectedDamage, player.calculateAtkDmg(),
                "The total damage of the player shall be " + expectedDamage +
                        " but it is " + player.calculateAtkDmg());
    }

    // Test regarding equiping a MasterSword
    @Test
    @Name("Player cannot equip MasterSword if he doesn't meet required lvl")
    public void testEquipWeaponMasterSwordNotSuccessfulPlayerLvlLow() {
        // spawning battleAxe for the player to equip
        Weapon masterSword = (Weapon) TreasureFactory.of("MasterSword");

        // testing does MasterSword has proper attackDmg and lvl required
        assertEquals(MasterSword.ATTACK_DMG, masterSword.getAttackDmg(),
                "The masterSword's attack dmg shall be " + MasterSword.ATTACK_DMG);
        assertEquals(MasterSword.LEVEL_REQ, masterSword.getLevelReq(),
                "The masterSword's lvl required shall be " + MasterSword.LEVEL_REQ);

        // the player doesnt not have the required level
        assertEquals(player.getCurrentLevel(), Player.PLAYER_STARTING_LEVEL,
                "The player level shall be " + Player.PLAYER_STARTING_LEVEL);
        assertFalse(player.equipWeapon(masterSword),
                "The masterSword cannot be equipped since the required level for it is " + MasterSword.LEVEL_REQ
                        + " and the player level is " + Player.PLAYER_STARTING_LEVEL);
    }
}
