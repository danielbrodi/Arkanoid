package animation;

import biuoop.DrawSurface;
import game.Counter;

import java.awt.Color;
import java.awt.Polygon;

/**
 * * A screen that indicates that the player won.
 * * Prints the final score of the player.
 */
public class EndScreenWin implements Animation {
    private boolean stop;
    private Counter score;

    /**
     * Instantiates a new "Win" screen.
     *
     * @param score the score
     */
    public EndScreenWin(Counter score) {
        this.stop = false;
        this.score = score;
    }

    /**
     * draws the screen.
     *
     * @param d the chosen Draw surface.
     */
    public void doOneFrame(DrawSurface d) {
        int[] xPnts = {42, 52, 72, 52, 60, 40, 15, 28, 9, 32, 42};
        int[] yPnts = {38, 62, 68, 80, 105, 85, 102, 75, 58, 60, 38};
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 11; j++) {
                if (xPnts[j] < 400) {
                    xPnts[j] = xPnts[j] + 33;
                }
                if (yPnts[j] < 300) {
                    yPnts[j] = yPnts[j] + 33;
                }
                ;
                if (xPnts[j] < 600) {
                    xPnts[j] = xPnts[j] * 2;
                }
                if (yPnts[j] < 500) {
                    yPnts[j] = yPnts[j] * 2;
                }
                ;
            }
            Polygon star = new Polygon(xPnts, yPnts, xPnts.length);
            d.setColor(Color.orange.darker());
            d.fillPolygon(star);
        }
        d.drawText(d.getWidth() / 5, d.getHeight() / 2 + 5, "You Win! Your score is " + score.getValue(), 35);
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