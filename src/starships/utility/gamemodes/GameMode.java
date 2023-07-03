package starships.utility.gamemodes;

import starships.entities.GameMap;

public abstract class GameMode {

    public enum winTypes {
        WIPEOUT
    }

    public boolean checkWinCondition(GameMap map) {return false;}

    public GameMode() {}
}
