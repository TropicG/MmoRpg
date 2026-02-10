package model.treasure.weapon;

import model.treasure.Treasure;

public abstract class Weapon extends Treasure {
    private final int levelReq;

    public Weapon(int attackDmg, int levelReq, String weaponName) {
        super(attackDmg, weaponName);
        this.levelReq = levelReq;
    }

    public int getAttackDmg() {
        return super.amplifierPoints;
    }

    public int getLevelReq() {
        return this.levelReq;
    }

    @Override
    public String toString() {
        return "Name: " + super.treasureName + ", atkDmg: " + super.amplifierPoints + ", lvlReq: " + levelReq + "\n";
    }
}
