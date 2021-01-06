package game;

import animation.AnimationRunner;
import animation.EndScreenLose;
import animation.EndScreenWin;
import animation.HighScoreScreen;
import animation.KeyPressStoppableAnimation;
import biuoop.KeyboardSensor;
import levels.LevelInformation;

import java.io.IOException;
import java.util.List;

/**
 * This class will be in charge of creating the different levels,
 * and moving from one level to the next.
 *
 * @author Daniel Brodsky
 */
public class GameFlow {
    private AnimationRunner animationRunner;
    private KeyboardSensor keyboardSensor;
    /**
     * The Sp.
     */
    private ScorePersistence sp = new ScorePersistence();

    /**
     * Instantiates a new Game flow.
     *
     * @param ar the animation runner
     * @param ks the keyboardSensor
     * @throws Exception the exception
     */
    public GameFlow(AnimationRunner ar, KeyboardSensor ks) throws Exception {
        this.animationRunner = ar;
        this.keyboardSensor = ks;
    }

    /**
     * Gets a list of levels and runs them in order.
     * Runs only if there are active blocks and balls in the level, if not, stop the animation and show
     * a lose screen.
     * If the play has passed all levels, the game will show him a "Win" screen.
     * Each level that is completed grants 100 points more points.
     * The score is saved from level to level till the end of the game.
     *
     * @param levels the levels
     * @throws IOException the io exception
     */
    public void runLevels(List<LevelInformation> levels) throws IOException {
        Counter score = new Counter(0);
        boolean isWin = true;
        for (LevelInformation levelInfo : levels) {

            GameLevel level = new GameLevel(levelInfo, this.animationRunner, this.keyboardSensor, score);

            level.initialize();

            while (level.getBlocksCount().getValue() != 0 && level.getBallsCount().getValue() != 0) {
                level.playOneTurn();
            }
            if (level.getBallsCount().getValue() == 0) {
                isWin = false;
                sp.updateFileIfHigher(score.getValue());
                this.animationRunner.run(new KeyPressStoppableAnimation(this.keyboardSensor,
                        "space", new EndScreenLose(score)));
                this.animationRunner.run(new KeyPressStoppableAnimation(this.keyboardSensor,
                        "space", new HighScoreScreen(sp.readFromFile())));
                break;
            }
            score.increase(100);
        }
        if (isWin) {
            sp.updateFileIfHigher(score.getValue());
            this.animationRunner.run(new KeyPressStoppableAnimation(this.keyboardSensor,
                    "space", new EndScreenWin(score)));
            this.animationRunner.run(new KeyPressStoppableAnimation(this.keyboardSensor,
                    "space", new HighScoreScreen(sp.readFromFile())));
        }
    }

}