package starships.ui;


import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;

public class GamePanel extends JPanel {
    private PlayerController player;

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
        this.addKeyListener(new GameKeyAdapter(player));
        this.addMouseListener(new GameMouseAdapter(player));
        this.setFocusable(true);
        this.setVisible(true);
    }

    private class GameKeyAdapter extends KeyAdapter {
        private PlayerController player;
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            char key = e.getKeyChar();
            player.selectAction(key);
            System.out.println("Key pressed: " + key);
            repaint();
        }

        public GameKeyAdapter(PlayerController player) {this.player = player;}
    }

    private class GameMouseAdapter extends MouseAdapter {
        private PlayerController player;

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
