package starships.ui;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ActionHandler extends AbstractAction {
    enum Action {
        MOVE_FORWARD, TURN_LEFT, TURN_RIGHT,
        STOP_MOVE_FORWARD, STOP_TURN_LEFT, STOP_TURN_RIGHT
    }
    private final Action action;
    private final PlayerController player;

    public ActionHandler(PlayerController player, Action action) {
        this.action = action;
        this.player = player;
    }

    @Override
    public void actionPerformed(ActionEvent e) { //pass the action to the player to decide what to do with it
        player.selectAction(action);
    }
}
