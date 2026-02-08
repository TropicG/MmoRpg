package model.treasure;

public abstract class Treasure {
    protected final int amplifierPoints;
    protected String treasureName;

    protected Treasure(int amplifierPoints, String treasureName){
        this.amplifierPoints = amplifierPoints;
        this.treasureName = treasureName;
    }

    public String getTreasureName() {
        return treasureName;
    }
}
