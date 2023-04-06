package starships.ui;


import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;

public class GamePanel extends JPanel {
    private final PlayerController player;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        AffineTransform at = new AffineTransform();
        at.rotate(
                Math.toRadians(player.getPlayerShip().getFacing()),
                player.getPlayerShip().getCenter().getX(),
                player.getPlayerShip().getCenter().getY()
        );
        g2.setTransform(at);
        g2.drawImage(
                player.getPlayerShip().getModel(),
                player.getPlayerShip().getPos().x,
                player.getPlayerShip().getPos().y,
                this
        );
        g.drawString("Facing: " + player.getPlayerShip().getFacing(), 20, 20);
        g2.dispose();
    }

    public GamePanel(PlayerController player) {
        this.player = player;
        this.setBackground(Color.white);
        this.setSize(800, 600);
        this.addKeyListener(new GameKeyAdapter());
        this.addMouseListener(new GameMouseAdapter(player));
        this.setFocusable(true);
        this.setVisible(true);
        this.setupKeybindings(this, player);
    }

    private void setupKeybindings(GamePanel panel, PlayerController player) {
        InputMap input = panel.getInputMap(WHEN_FOCUSED);
        ActionMap actions = panel.getActionMap();

        input.put(KeyStroke.getKeyStroke('w'), ActionHandler.Action.MOVE_FORWARD);
        input.put(KeyStroke.getKeyStroke('a'), ActionHandler.Action.TURN_LEFT);
        input.put(KeyStroke.getKeyStroke('d'), ActionHandler.Action.TURN_RIGHT);

        actions.put(ActionHandler.Action.MOVE_FORWARD, new ActionHandler(player, ActionHandler.Action.MOVE_FORWARD));
        actions.put(ActionHandler.Action.TURN_LEFT, new ActionHandler(player, ActionHandler.Action.TURN_LEFT));
        actions.put(ActionHandler.Action.TURN_RIGHT, new ActionHandler(player, ActionHandler.Action.TURN_RIGHT));
    }

    private class GameKeyAdapter extends KeyAdapter {
        //private PlayerController player;

        //Set<Integer> keys = new HashSet<Integer>();
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            /*keys.add(e.getKeyCode());
            for (Integer key : keys) {
                System.out.println(key);
                player.selectAction(key);
            }*/
            repaint();
        }

       /* @Override
        public void keyReleased(KeyEvent e) {
            super.keyReleased(e);
            keys.remove(e.getKeyCode());
        }*/

       // public GameKeyAdapter(PlayerController player) {this.player = player;}
    }

    private class GameMouseAdapter extends MouseAdapter {
        private final PlayerController player;

        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            Point clickPos = e.getLocationOnScreen();
            int button = e.getButton();
            player.selectAction(clickPos, button);
            repaint();
        }

        public GameMouseAdapter(PlayerController player) {this.player = player;}
    }
}
