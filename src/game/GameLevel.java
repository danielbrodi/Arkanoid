package game;

import animation.Animation;
import animation.AnimationRunner;
import animation.PauseScreen;
import animation.KeyPressStoppableAnimation;
import animation.CountdownAnimation;
import ball.Ball;
import ball.Velocity;
import biuoop.DrawSurface;
import biuoop.KeyboardSensor;
import collidable.Block;
import collidable.Collidable;
import collidable.Paddle;
import geometry.Point;
import geometry.Rectangle;
import levels.LevelInformation;
import listeners.LevelNameIndicator;
import listeners.BallRemover;
import listeners.BlockRemover;
import listeners.ScoreIndicator;
import listeners.ScoreTrackingListener;

import java.awt.Color;
import java.util.List;

/**
 * The main game environment, gets a level, creates the game level window, the objects and their design.
 *
 * @author Daniel Brodsky
 */
public class GameLevel implements Animation {
    private LevelInformation level;
    private SpriteCollection sprites;
    private GameEnvironment environment;
    private Counter countBlocks;
    private Counter countBalls;
    private Counter countScore;
    private BlockRemover blockRemover;
    private BallRemover ballRemover;
    private ScoreTrackingListener scoreTracker;
    /**
     * The constant width of the gui window that runs the game.
     */
    private static double guiWidth = 800;
    /**
     * The constant height of the gui window that runs the game.
     */
    private static double guiHeight = 600;
    private static Point guiStart = new Point(0, 0);
    private static double blockWidth = 35;
    private double paddleWidth;
    private double paddleHeight;
    private AnimationRunner runner;
    private boolean running;
    private KeyboardSensor keyboard;

    /**
     * Instantiates a new level, get information and creates the sprites.
     *
     * @param level the given level
     * @param ar    the animation runner
     * @param ks    the keyboardSensor
     * @param score the given score
     */
    public GameLevel(LevelInformation level, AnimationRunner ar, KeyboardSensor ks, Counter score) {
        this.sprites = new SpriteCollection();
        this.environment = new GameEnvironment();
        this.keyboard = ks;
        this.runner = ar;
        countScore = score;
        this.level = level;
        paddleWidth = level.paddleWidth();
        paddleHeight = 20;
    }

    /**
     * Adds collidable to the game.
     *
     * @param c the collidable
     */
    public void addCollidable(Collidable c) {
        environment.addCollidable(c);
    }

    /**
     * Removes a collidable from the game environment.
     *
     * @param c the collidable
     */
    public void removeCollidable(Collidable c) {
        environment.removeCollidable(c);
    }

    /**
     * Adds sprite to the game.
     *
     * @param s the sprite
     */
    public void addSprite(Sprite s) {
        sprites.addSprite(s);
    }

    /**
     * Removes sprite from the game.
     *
     * @param s the sprite
     */
    public void removeSprite(Sprite s) {
        sprites.removeSprite(s);
    }

    /**
     * Create side border blocks that will prevent the ball of getting out of bounds(the active game window).
     * Also creates a death region, a bottom block that will remove any ball that hits it.
     */
    public void createSideBlocks() {
        Point[] guiBorderPoints = new Point[4];
        guiBorderPoints[0] = guiStart;
        guiBorderPoints[1] = new Point(guiWidth - 10, guiStart.getY());
        guiBorderPoints[2] = new Point(guiStart.getX(), guiHeight - 10);
        guiBorderPoints[3] = guiStart;
        Double[] borderBlockHeight = new Double[4];
        borderBlockHeight[0] = blockWidth - 10;
        borderBlockHeight[1] = guiHeight;
        borderBlockHeight[2] = (double) 1;
        borderBlockHeight[3] = guiHeight;
        Double[] borderBlockWidth = new Double[4];
        borderBlockWidth[0] = guiWidth;
        double blockHeight = 20;
        borderBlockWidth[1] = blockHeight;
        borderBlockWidth[2] = guiWidth;
        borderBlockWidth[3] = blockHeight - 10;
        for (int i = 0; i < 4; i++) {
            Rectangle rectangle = new Rectangle(guiBorderPoints[i], borderBlockWidth[i], borderBlockHeight[i]);
            Block block = new Block(rectangle, Color.gray);
            block.addToGame(this);
            if (i == 2) {
                block.addHitListener(ballRemover);
                block.removeFromSprites(this);
            }
        }
    }

    /**
     * Creates and adds white balls with a given velocity to the game.
     * The velocity for each ball will be given by the level, as a list of velocities sorted in order.
     * The amount of the needed balls will be also given by the level.
     */
    public void createBalls() {
        List<Velocity> velocities = level.initialBallVelocities();
        for (int i = 0; i < velocities.size(); i++) {
            int ballRadius = 5;
            Ball ball = new Ball(new Point((float) guiWidth / 2, guiHeight - blockWidth - paddleHeight),
                    ballRadius, Color.white, environment,
                    velocities.get(i));
            ball.addToGame(this);
        }
    }

    /**
     * Creates and adds blocks to the game, in a given pattern by every level, with a
     * different color and a different number of blocks for each level.
     * The blocks and the location of each one of them will be given by the level as a list of blocks.
     */
    public void createBlocks() {
        for (Block block : this.level.blocks()) {
            block.addToGame(this);
            block.addHitListener(blockRemover);
            block.addHitListener(scoreTracker);
        }
    }

    /**
     * Creates a score board.
     */
    private void scoreIndicator() {
        Rectangle rectangle = new Rectangle(guiStart, guiWidth, 15);
        Block block = new Block(rectangle, Color.white);
        block.addToGame(this);
        ScoreIndicator scoreBoard = new ScoreIndicator(countScore);
        scoreBoard.addToGame(this);
    }

    /**
     * keeps the name of the level in the upper part of the gui,
     * at the left corner of the window.
     */
    private void levelNameIndicator() {
        LevelNameIndicator levelName = new LevelNameIndicator(level.levelName());
        levelName.addToGame(this);
    }

    /**
     * Creates a paddle that will be able to move by the keyboard input and hit the ball.
     * The stats of the paddle as width and speed will be given by the level.
     *
     * @param theKeyboard the given keyboard.
     */
    public void createPaddle(KeyboardSensor theKeyboard) {
        Rectangle paddle = new Rectangle(new Point((guiWidth / 2) - (paddleWidth / 2), guiHeight - blockWidth),
                paddleWidth, paddleHeight);
        Paddle p = new Paddle(paddle, Color.yellow, theKeyboard, level.paddleSpeed());
        p.addToGame(this);
    }

    /**
     * Initializes the games. creates a gui and adds the objects: balls, paddle, blocks, borders, score board
     * and a level name indicator.
     * Also creates the counters for the balls, the blocks and the score.
     */
    public void initialize() {
        if (level.getBackground() != null) {
            sprites.addSprite(level.getBackground());
        }
        countBlocks = new Counter(level.numberOfBlocksToRemove());
        countBalls = new Counter(level.numberOfBalls());
        blockRemover = new BlockRemover(this, countBlocks);
        ballRemover = new BallRemover(this, countBalls);
        scoreTracker = new ScoreTrackingListener(countScore);
        createBlocks();
        createSideBlocks();
        createPaddle(keyboard);
        scoreIndicator();
        levelNameIndicator();
    }

    /**
     * Run the game -- start the animation loop.
     * In charge of what will be done in every single frame.
     * Adds the option to pause the animation by pressing "p" and
     * return to the exact point by pressing "space".
     * Stops the animation loop if there are no more balls or blocks that left in the level.
     */

    @Override
    public void doOneFrame(DrawSurface d) {
        if (this.keyboard.isPressed("p")) {
            this.runner.run(new KeyPressStoppableAnimation(this.keyboard,
                    "space", new PauseScreen()));
        }
        this.sprites.drawAllOn(d);
        this.sprites.notifyAllTimePassed();
        if (countBlocks.getValue() == 0 || countBalls.getValue() == 0) {
//            if (countBalls.getValue() == 0) {
//                this.running = false;
//            }
            this.running = false;
        }
    }

    /**
     * Stops the animation if there is already a reuqest to stop the animation loop.
     *
     * @return true of false
     */
    @Override
    public boolean shouldStop() {
        return !this.running;
    }

    /**
     * Plays one turn, defines what happens when a new turn starts.
     * Creates balls,
     * runs a countdown animation before the turn starts,
     * and starts the loop animation of the turn.
     */
    public void playOneTurn() {
        createBalls();
        this.runner.run(new CountdownAnimation(2, 3, sprites));
        this.running = true;
        this.runner.run(this);
    }

    /**
     * Gets current blocks count.
     *
     * @return the count
     */
    public Counter getBlocksCount() {
        return countBlocks;
    }

    /**
     * Gets the current balls count.
     *
     * @return the count
     */
    public Counter getBallsCount() {
        return countBalls;
    }

    /**
     * Gets gui height.
     *
     * @return the gui's height
     */
    public static double getGuiHeight() {
        return guiHeight;
    }

    /**
     * Gets gui width.
     *
     * @return the gui's width
     */
    public static double getGuiWidth() {
        return guiWidth;
    }

}