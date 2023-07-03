package starships.utility.gamemodes;

import starships.entities.GameMap;
import starships.ui.ScorePanel;

public class GameModeSkirmish extends GameMode{

    private int victoriousTeam = -1;
    private winTypes winType;

    public GameModeSkirmish() {
        super();
    }

    @Override
    public boolean checkWinCondition(GameMap map) {
        if(!map.isTeamAlive(0)) {
            if(map.isTeamAlive(1)) {
                this.victoriousTeam = 1;
            } else {
                this.victoriousTeam = -1;
            }
            this.winType = winTypes.WIPEOUT;
            return true;
        } else if (!map.isTeamAlive(1)) {
            if(map.isTeamAlive(1)) {
                this.victoriousTeam = 1;
            } else {
                this.victoriousTeam = -1;
            }
            this.winType = winTypes.WIPEOUT;
            return true;
        }
        return false;
    }

    public ScorePanel endGame() {
        return new ScorePanel(this.victoriousTeam, this.winType);
    }
}
