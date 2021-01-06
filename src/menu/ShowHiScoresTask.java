package menu;

import animation.Animation;
import animation.AnimationRunner;
import animation.HighScoreScreen;
import animation.KeyPressStoppableAnimation;
import biuoop.KeyboardSensor;
import game.ScorePersistence;

import java.io.IOException;

/**
 * The type Show high scores task.
 *
 * @author Daniel Brodsky
 */
public class ShowHiScoresTask implements Task<Void> {
    private Animation highScoresAnimation;
    private AnimationRunner runner;
    /**
     * The Keyboard sensor.
     */
    private KeyboardSensor keyboardSensor;
    /**
     * The Sp.
     */
    private ScorePersistence sp;

    /**
     * Instantiates a new Show high scores task.
     *
     * @param runner the runner
     * @param ks     the ks
     * @throws Exception the exception
     */
    public ShowHiScoresTask(AnimationRunner runner, KeyboardSensor ks) throws Exception {
        this.runner = runner;
        keyboardSensor = ks;
        this.sp = new ScorePersistence();
    }

    /**
     * runs the animation and shows the screen of the current high score.
     *
     * @return null
     * @throws IOException if there is a problem runing this animation
     */
    public Void run() throws IOException {
        highScoresAnimation = new KeyPressStoppableAnimation(keyboardSensor,
                "space", new HighScoreScreen(sp.readFromFile()));
        this.runner.run(this.highScoresAnimation);
        return null;
    }
}
