package animation;

import biuoop.DrawSurface;
import game.Counter;

import java.awt.Polygon;
import java.awt.Color;

/**
 * A screen that indicates that the player lost.
 * Prints the final score of the player.
 *
 * @author Daniel Brodsky
 */
public class EndScreenLose implements Animation {
    private boolean stop;
    private Counter score;

    /**
     * Instantiates a new End screen lose.
     *
     * @param score the score
     */
    public EndScreenLose(Counter score) {
        this.stop = false;
        this.score = score;
    }

    /**
     * draws the screen.
     *
     * @param d the chosen Draw surface.
     */
    public void doOneFrame(DrawSurface d) {
        d.setColor(Color.PINK);
        d.fillOval(10, 30, 180, 150);

        d.setColor(Color.white);
        d.fillOval(50, 66, 35, 53);
        d.fillOval(78, 66, 35, 53);

        d.setColor(Color.black);
        d.fillOval(63, 86, 10, 10);
        d.fillOval(90, 86, 10, 10);

        d.setColor(Color.black);
        int[] xValues = {56, 89, 109};
        int[] yValues = {140, 150, 140};
        Polygon mouth = new Polygon(xValues, yValues, 3);
        d.fillPolygon(mouth);
        d.drawText(d.getWidth() / 5, d.getHeight() / 2, "Game Over. Your score is " + score.getValue(), 35);
    }

    /**
     * when should it stop.
     *
     * @return false or true
     */
    public boolean shouldStop() {
        return this.stop;
    }
}