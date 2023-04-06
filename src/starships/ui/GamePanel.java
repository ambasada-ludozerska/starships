package starships.ui;


import starships.entities.GameMap;
import starships.entities.Ship;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;

public class GamePanel extends JPanel {
    private final PlayerController player;
    private final GameMap map;

    @Override
    public void paintComponent(Graphics g) { //TODO - add drawing of static objects
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        AffineTransform at = new AffineTransform();
        for (Ship s : map.getAllShips().keySet()) {
            at.rotate(
                    Math.toRadians(s.getFacing()),
                    s.getCenter().getX(),
                    s.getCenter().getY()
            );
            g2.setTransform(at);
            g2.drawImage(
                    s.getModel(),
                    s.getPos().x,
                    s.getPos().y,
                    this
            );
        }
        
        g.drawString("Facing: " + player.getPlayerShip().getFacing(), 20, 20);
        g2.dispose();
    }

    public GamePanel(GameMap map) {
        this.map = map;
        this.player = map.getPlayer();
        this.setBackground(Color.white);
        this.setSize(1000, 800);
        this.addMouseListener(new GameMouseAdapter(player));
        this.setFocusable(true);
        this.setVisible(true);
        this.setupKeybindings(this, player);
    }

    private void setupKeybindings(GamePanel panel, PlayerController player) { //TODO - handle multiple keys
        InputMap input = panel.getInputMap(WHEN_FOCUSED);
        ActionMap actions = panel.getActionMap();

        input.put(KeyStroke.getKeyStroke('w'), ActionHandler.Action.MOVE_FORWARD);
        input.put(KeyStroke.getKeyStroke('a'), ActionHandler.Action.TURN_LEFT);
        input.put(KeyStroke.getKeyStroke('d'), ActionHandler.Action.TURN_RIGHT);

        actions.put(ActionHandler.Action.MOVE_FORWARD, new ActionHandler(player, ActionHandler.Action.MOVE_FORWARD));
    }
    private class GameMouseAdapter extends MouseAdapter {
        private final PlayerController player;

        @Override
        public void mouseClicked(MouseEvent e) { //TODO - mouse targeting
            super.mouseClicked(e);
            Point clickPos = e.getLocationOnScreen();
            int button = e.getButton();
            player.selectAction(clickPos, button);
            repaint();
        }

        public GameMouseAdapter(PlayerController player) {this.player = player;}
    }
}
