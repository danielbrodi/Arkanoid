package animation;

import biuoop.DrawSurface;

/**
 * A screen that indicates that the player lost.
 * Prints the final score of the player.
 *
 * @author Daniel Brodsky
 */
public class HighScoreScreen implements Animation {
    private boolean stop;
    private int score;

    /**
     * Instantiates a new End screen lose.
     *
     * @param score the score
     */
    public HighScoreScreen(int score) {
        this.stop = false;
        this.score = score;
    }

    /**
     * draws the screen.
     *
     * @param d the chosen Draw surface.
     */
    public void doOneFrame(DrawSurface d) {
        d.drawText(d.getWidth() / 5, d.getHeight() / 2, "High Score is " + score, 35);
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