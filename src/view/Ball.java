package view;

import java.awt.*;


/**
 * Created by Dennis on 29/05/2016.
 */
public class Ball implements Runnable {
    private double x, y, fartx, farty;
    private int size;
    private Color color;
    private Graphics g;
    private int sizeJFrameX = 1000;
    private int sizeJFrameY = 800;

    public Ball(Graphics g1, int x1, int y1, Color c, int d1) {
        g = g1;
        x = x1;
        y = y1;
        color = c;
        size = d1;
        fartx = Math.random() * 50;
        farty = Math.random() * 60;

        Thread t = new Thread(this);
        t.start();
    }


    public void moveBall() {
        while (true) {
            synchronized(g) {
                // Slet bold: Tegn bolden over med hvid på den gamle position
                g.setColor(Color.WHITE);
                g.fillOval((int) x, (int) y, size, size);
                // Opdater positionen med farten
                x = x + fartx;
                y = y + farty;
                // Tegn bolden på den nye position
                g.setColor(color);
                g.fillOval((int) x, (int) y, size, size);
                // Hvis bolden er uden for det tilladte område skal den
                // rettes hen mod området
                if (x < 0) fartx = Math.abs(fartx);
                if (x + size > sizeJFrameX) fartx = -Math.abs(fartx);
                if (y < 0) farty = Math.abs(farty);
                if (y + size > sizeJFrameY) farty = -Math.abs(farty);
            }
            // Vent lidt
            try {
                Thread.sleep(50);
            } catch (Exception e) {
            }
        }
    }


    public void run() {
        moveBall();
    }
}
