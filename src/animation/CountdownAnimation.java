package animation;

import biuoop.Sleeper;
import biuoop.DrawSurface;
import game.SpriteCollection;

import java.awt.Color;


/**
 * The type Countdown animation.
 * On-screen countdown from 3 to 1, which will show up at the beginning of each turn.
 * Only after the countdown reaches zero, things will start moving and we will start with the game play.
 * Lasts 2 seconds.
 *
 * @author Daniel Brodsky
 */
public class CountdownAnimation implements Animation {

    private double numOfSeconds;
    private int countFrom;
    private SpriteCollection gameScreen;
    private boolean stop;

    /**
     * Instantiates a new Countdown animation.
     *
     * @param numOfSeconds the number of seconds the animation is going to last.
     * @param countFrom    the number the counter will count from.
     * @param gameScreen   the game screen.
     */
    public CountdownAnimation(double numOfSeconds,
                              int countFrom,
                              SpriteCollection gameScreen) {
        this.numOfSeconds = numOfSeconds;
        this.countFrom = countFrom;
        this.gameScreen = gameScreen;
        this.stop = false;

    }

    /**
     * draws the level and freezes the game animation it for 2 seconds while counting down from 3 to 1.
     * Then the level starts.
     *
     * @param d the chosen Draw surface.
     */
    @Override
    public void doOneFrame(DrawSurface d) {
        gameScreen.drawAllOn(d);
        Sleeper mySleeper = new Sleeper();
        d.setColor(Color.red);
        d.drawText(d.getWidth() / 2, d.getHeight() / 2, Integer.toString(countFrom--), 66);

        if (this.countFrom == 2) {
            return;
        }
        numOfSeconds--;
        mySleeper.sleepFor(1000);
        if (numOfSeconds < 0) {
            this.stop = true;
//        }
        }
    }

    /**
     * when should it stop.
     *
     * @return true or false
     */
    public boolean shouldStop() {
        return this.stop;
    }
}