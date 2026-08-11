package model.treasure;

import java.util.Objects;

public abstract class Treasure {
    protected final int amplifierPoints;
    protected String treasureName;

    protected Treasure(int amplifierPoints, String treasureName) {
        this.amplifierPoints = amplifierPoints;
        this.treasureName = treasureName;
    }

    public String getTreasureName() {
        return treasureName;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(treasureName);
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        Treasure otherTreasure = (Treasure) obj;
        return treasureName.equals(otherTreasure.treasureName);
    }

}
