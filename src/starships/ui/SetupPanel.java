package starships.ui;

import starships.GameInit;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static starships.GameInit.shipClasses.BC;
import static starships.GameInit.shipClasses.DD;

public class SetupPanel extends JPanel {

    public SetupPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    }

    public void init() {
        SetupListener listener = new SetupListener(new GameInit((GameWindow) SwingUtilities.getRoot(this)));
        this.add(new JLabel("Game setup"));

        this.add(new JLabel("Select ship class"));

        JButton selectBCButton = new JButton("Battlecruiser");
        selectBCButton.setActionCommand("selectShipBC");
        this.add(selectBCButton);
        selectBCButton.addActionListener(listener);

        JButton selectDDButton = new JButton("Destroyer");
        selectDDButton.setActionCommand("selectShipDD");
        this.add(selectDDButton);
        selectDDButton.addActionListener(listener);

        JButton startButton = new JButton("Start game");
        startButton.setActionCommand("startGame");
        this.add(startButton);
        startButton.addActionListener(listener);
    }

    static class SetupListener implements ActionListener {

        private final GameInit init;

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()) {
                case "startGame" -> init.launch();
                case "selectShipBC" -> init.selectPlayerShip(BC);
                case "selectShipDD" -> init.selectPlayerShip(DD);
            }
        }

        public SetupListener(GameInit init) {
            this.init = init;
        }
    }
}
