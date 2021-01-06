package animation;

import biuoop.DrawSurface;

import java.awt.Color;

/**
 * A kind of Animation that will display a screen with a message until a key is pressed.
 * The purpose of this animation is to pause the game and give an option to be able to return to the
 * exact point.
 *
 * @author Daniel Brodsky
 */
public class PauseScreen implements Animation {
    private boolean stop;


    /**
     * Instantiates a new Pause screen.
     */
    public PauseScreen() {
        this.stop = false;
    }

    /**
     * Draws and creates the design of the pause screen.
     *
     * @param d the chosen Draw surface.
     */
    public void doOneFrame(DrawSurface d) {
        int yep = 0;
        int w = d.getWidth(), h = d.getHeight();
        d.setColor(Color.black);
        d.fillRectangle(0, 0, w, h);
        d.setColor(new Color(128, 0, 255));
        d.setColor(Color.darkGray.darker());
        for (int r = yep++ % 10; r < w; r += 10) {
            d.drawOval(w / 2 - r, h / 2 - r, 2 * r, 2 * r);
        }
        int s = (int) (4 * Math.sin(.3 * yep));

        d.setColor(Color.darkGray);
        d.fillRectangle(90, 150, 50, 150);
        d.drawRectangle(90, 150, 50, 150);
        d.setColor(Color.darkGray);
        d.fillRectangle(90, 150, 50, 150);
        d.drawRectangle(90, 150, 50, 150);
        d.setColor(Color.darkGray);
        d.fillRectangle(105, 300, 20, 155);
        d.drawRectangle(105, 300, 20, 155);
        d.drawOval(90, 150, 50, 50);
        d.drawOval(90, 200, 50, 50);
        d.drawOval(90, 250, 50, 50);
        d.setColor(Color.red);
        d.fillOval(90, 150, 50, 50);
        d.drawOval(90, 150, 50, 50);

        d.setColor(Color.white);
        d.drawText(140, 350, "paused -- press space to continue", 36);
    }

    /**
     * Checks whether this animation should stop.
     *
     * @return true or false.
     */
    public boolean shouldStop() {
        return this.stop;
    }
}