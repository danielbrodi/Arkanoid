package listeners;

import game.Counter;
import collidable.Block;
import ball.Ball;

/**
 * HitListener called ScoreTrackingListener to update the game's
 * counter when blocks are being hit and removed.
 *
 * @author Daniel Brodsky
 */
public class ScoreTrackingListener implements HitListener {
    private Counter score;

    /**
     * Instantiates a new Score tracking listener.
     *
     * @param scoreCounter the score counter
     */
    public ScoreTrackingListener(Counter scoreCounter) {
        score = scoreCounter;
    }

    /**
     * Adds 5 to the current score when a block being hit.
     *
     * @param beingHit the block that being hit
     * @param hitter   the hitter is the ball.Ball that's doing the hitting.
     */
    public void hitEvent(Block beingHit, Ball hitter) {
        score.increase(5);
    }
}