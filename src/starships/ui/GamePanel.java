package starships.ui;

import starships.controllers.PlayerController;
import starships.entities.GameMap;
import starships.entities.Projectile;
import starships.entities.Ship;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;

public class GamePanel extends JPanel {
    private final PlayerController player;
    private final GameMap map;

    @Override
    public void paintComponent(Graphics g) { //TODO - add drawing of static objects
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        /*RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHints(rh);*/
        for (Ship s : map.getAllShips().keySet()) {
            AffineTransform at = new AffineTransform();
            at.rotate(
                    Math.toRadians(s.getFacing()),
                    s.getPos().getX(),
                    s.getPos().getY()
            );
            g2.setTransform(at);
            g2.drawImage(
                    s.getModel(),
                    (int) Math.round(s.getImgPos().getX()),
                    (int) Math.round(s.getImgPos().getY()),
                    this
            );
            at.rotate(0, 0, 0);
            g2.setTransform(at);
        }
        for (Projectile p : map.getActiveProjectiles()) {
            g.drawOval((int) p.getImgPos().getX(), (int) p.getImgPos().getY(), p.getSize(), p.getSize());
        }
        
        g.drawString("Facing: " + player.getShip().getFacing(), 20, 20);
        g.drawString("Hull integrity: " + player.getShip().getRemainingHullIntegrity(), 100, 20);
        g2.dispose();
    }

    public GamePanel(GameMap map) {
        this.map = map;
        this.player = map.getLocalPlayer();
        this.setBackground(Color.white);
        //this.setSize(1920, 1080);
        this.addMouseListener(new GameMouseAdapter(player));
        this.addMouseMotionListener((MouseMotionListener) this.getMouseListeners()[0]);
        this.setFocusable(true);
        this.setVisible(true);
        this.setupKeybindings(this, player);
    }

    private void setupKeybindings(GamePanel panel, PlayerController player) {
        InputMap input = panel.getInputMap(WHEN_FOCUSED);
        ActionMap actions = panel.getActionMap();
        //keybindings for start actions
        input.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false), ActionHandler.Action.MOVE_FORWARD);
        input.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false), ActionHandler.Action.TURN_LEFT);
        input.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false), ActionHandler.Action.TURN_RIGHT);
        //keybindings for stop actions
        input.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, true), ActionHandler.Action.STOP_MOVE_FORWARD);
        input.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true), ActionHandler.Action.STOP_TURN_LEFT);
        input.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true), ActionHandler.Action.STOP_TURN_RIGHT);

        //start actions themselves
        actions.put(ActionHandler.Action.MOVE_FORWARD, new ActionHandler(player, ActionHandler.Action.MOVE_FORWARD));
        actions.put(ActionHandler.Action.TURN_LEFT, new ActionHandler(player, ActionHandler.Action.TURN_LEFT));
        actions.put(ActionHandler.Action.TURN_RIGHT, new ActionHandler(player, ActionHandler.Action.TURN_RIGHT));
        //stop actions themselves
        actions.put(ActionHandler.Action.STOP_MOVE_FORWARD, new ActionHandler(player, ActionHandler.Action.STOP_MOVE_FORWARD));
        actions.put(ActionHandler.Action.STOP_TURN_LEFT, new ActionHandler(player, ActionHandler.Action.STOP_TURN_LEFT));
        actions.put(ActionHandler.Action.STOP_TURN_RIGHT, new ActionHandler(player, ActionHandler.Action.STOP_TURN_RIGHT));
    }
    private static class GameMouseAdapter extends MouseAdapter {
        private final PlayerController player;

        @Override
        public void mouseDragged(MouseEvent e) {
            super.mouseDragged(e);
            Point clickPos = e.getLocationOnScreen();
            player.updateTargetPosition(clickPos);
            int button = e.getButton();
            player.selectAction(button, false);
        }
        @Override
        public void mouseMoved(MouseEvent e) {
            super.mouseMoved(e);
            Point clickPos = e.getLocationOnScreen();
            player.updateTargetPosition(clickPos);
        }
        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            int button = e.getButton();
            player.selectAction(button, false);
        }
        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            int button = e.getButton();
            player.selectAction(button, true);
        }

        public GameMouseAdapter(PlayerController player) {this.player = player;}
    }
}
