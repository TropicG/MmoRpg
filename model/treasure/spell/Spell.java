package model.treasure.spell;

import model.treasure.Treasure;

public class Spell extends Treasure {
    private final int levelReq;
    private final int manaCost;

    public Spell(int attackDmg, int levelReq, int manaCost, String weaponName) {
        super(attackDmg, weaponName);
        this.levelReq = levelReq;
        this.manaCost = manaCost;
    }

    public int getAttackDmg() {
        return super.amplifierPoints;
    }

    public int getManaCost() {
        return this.manaCost;
    }

    public int getLevelReq() {
        return this.levelReq;
    }

    @Override
    public String toString() {
        return "Name: " + super.treasureName + ", atkDmg: " + super.amplifierPoints
                + ", manaCost: " + manaCost + ", lvlReq: " + levelReq + "\n";
    }
}
