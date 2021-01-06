package collidable;

import ball.Ball;
import ball.Velocity;
import biuoop.DrawSurface;
import biuoop.KeyboardSensor;
import geometry.Point;
import geometry.Rectangle;
import game.GameLevel;
import game.Sprite;

import java.awt.Color;

/**
 * The collidable Paddle is the player in the game.
 * It is a rectangle that is controlled by the arrow keys, and moves according to the player key presses.
 * Implements the mainGame.Sprite and the collidable.Collidable interfaces.
 * Moves to the left and to the right.
 *
 * @author Daniel Brodsky
 */
public class Paddle implements Sprite, Collidable {
    private static int step;
    private Color color;
    private Rectangle paddle;
    private static double guiWidthLeft = 10;
    private static double guiWidthRight = 780;
    private biuoop.KeyboardSensor keyboard;
    private double[] regionBorders = new double[4];

    /**
     * Instantiates a new Paddle.
     *
     * @param paddle   the rectangle
     * @param color    the color
     * @param keyboard the keyboard
     * @param thespeed speed of the paddle, given by each level
     */
    public Paddle(Rectangle paddle, Color color, biuoop.KeyboardSensor keyboard, int thespeed) {
        this.color = color;
        this.paddle = paddle;
        this.keyboard = keyboard;
        step = thespeed;
    }

    /**
     * Moves left if the paddle isn't going out of bounds.
     */
    public void moveLeft() {
        if (paddle.getUpperLeft().getX() - step <= guiWidthLeft) {
            this.paddle = new Rectangle(new Point(guiWidthLeft + step + 2,
                    paddle.getUpperLeft().getY()), paddle.getWidth(), paddle.getHeight());
        }
        this.paddle = new Rectangle(new Point(paddle.getUpperLeft().getX() - step,
                paddle.getUpperLeft().getY()), paddle.getWidth(), paddle.getHeight());
    }

    /**
     * Moves right if the paddle isn't going out of bounds.
     */
    public void moveRight() {
        if (paddle.getUpperLeft().getX() + step + paddle.getWidth() >= guiWidthRight) {
            this.paddle = new Rectangle(new Point(guiWidthRight - paddle.getWidth() - step + 5,
                    paddle.getUpperLeft().getY()), paddle.getWidth(), paddle.getHeight());
        }
        this.paddle = new Rectangle(new Point(paddle.getUpperLeft().getX() + step,
                paddle.getUpperLeft().getY()), paddle.getWidth(), paddle.getHeight());
    }

    /**
     * draws the paddle.
     *
     * @param d the drawing surface
     */
    public void drawOn(DrawSurface d) {
        d.setColor(color);
        d.drawRectangle((int) this.paddle.getUpperLeft().getX(),
                (int) this.paddle.getUpperLeft().getY(),
                (int) this.paddle.getWidth(), (int) this.paddle.getHeight());
        d.fillRectangle((int) paddle.getUpperLeft().getX(),
                (int) this.paddle.getUpperLeft().getY(),
                (int) this.paddle.getWidth(), (int) this.paddle.getHeight());
        d.setColor(Color.black);
        d.drawRectangle((int) this.paddle.getUpperLeft().getX(),
                (int) this.paddle.getUpperLeft().getY(),
                (int) this.paddle.getWidth(), (int) this.paddle.getHeight());
    }

    /**
     * checks whether the keyboard arrow that was pressed is the right arrow or the left one,
     * and tries to move the paddle.
     */
    public void timePassed() {
        if (this.keyboard.isPressed(KeyboardSensor.LEFT_KEY)) {
            this.moveLeft();
        }
        if (this.keyboard.isPressed(KeyboardSensor.RIGHT_KEY)) {
            this.moveRight();
        }
    }

    /**
     * returns the paddle's info.
     *
     * @return this.paddle
     */
    public Rectangle getCollisionRectangle() {
        return this.paddle;
    }

    /**
     * checkRegion.
     * check for the area of the impact.
     * Splits the paddle for 5 different parts .
     *
     * @param collisionPoint - the collision point with the paddle
     * @return the location in numbers 1 - 5 (or 0 if there is no impact).
     */
    public int checkRegion(Point collisionPoint) {
        double paddleStartingX = paddle.getUpperLeft().getX();
        // because we have 5 regions
        double collisionPointX = collisionPoint.getX();
        for (int i = 0; i < 4; i++) {
            regionBorders[i] = (paddle.getWidth() / 4) * i + 1;
        }
        if (collisionPointX >= paddleStartingX && collisionPointX <= paddleStartingX + regionBorders[0]) {
            return 1; //hit region 1
        }
        if (collisionPointX >= paddleStartingX + regionBorders[0]
                && collisionPointX <= paddleStartingX + regionBorders[1]) {
            return 2;
        }
        if (collisionPointX >= paddleStartingX + regionBorders[1]
                && collisionPointX <= paddleStartingX + regionBorders[2]) {
            return 3;
        }
        if (collisionPointX >= paddleStartingX + regionBorders[2]
                && collisionPointX <= paddleStartingX + regionBorders[3]) {
            return 4;
        }
        if (collisionPointX >= paddleStartingX + regionBorders[3]
                && collisionPointX <= paddleStartingX + paddle.getWidth()) {
            return 5;
        }
        return 0;

    }

    /**
     * changeVelocity.
     * change the velocity when hits certain region ,
     * and puts it in the array of the velocities (velo).
     *
     * @param speed   - the speed of the ball before the hit.
     * @param velo    - an array of the velocities of each region.
     * @param currVel - the current velocity of the ball.
     */
    public void changeVelocity(double speed, Velocity[] velo,
                               Velocity currVel) {
        double dx = currVel.getDx();
        double dy = currVel.getDy();
        velo[1] = Velocity.fromAngleAndSpeed(-60, speed);
        velo[2] = Velocity.fromAngleAndSpeed(-30, speed);
        velo[3] = new Velocity(dx, -dy);
        velo[4] = Velocity.fromAngleAndSpeed(30, speed);
        velo[5] = Velocity.fromAngleAndSpeed(60, speed);
        velo[0] = new Velocity(dx, dy);
    }

    /**
     * directionChange.
     * create ball.Velocity from x and y coordinates by transforming
     * it into an angle and speed.
     *
     * @param collisionPoint - the collision point.
     * @param region         - number of the region (1 - 5).
     * @param currVel        - current velocity.
     * @return new velocity
     */
    public Velocity angleChange(Point collisionPoint,
                                int region, Velocity currVel) {
        Velocity[] velo = new Velocity[6];
        double speed = Math.sqrt((currVel.getDx() * currVel.getDx())
                + (currVel.getDy() * currVel.getDy()));
        changeVelocity((int) speed, velo, currVel);
        Velocity v = velo[region];
        return v;
    }

    /**
     * hit.
     * return the new velocity of an object after a hit with an other object.
     *
     * @param collisionPoint  - is the collision point.
     * @param currentVelocity - given the current velocity.
     * @param hitter          - the ball that hits the block
     * @return new velocity.
     */
    public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity) {
        int region = this.checkRegion(collisionPoint);
        return angleChange(collisionPoint, region, currentVelocity);
    }

    /**
     * Adds the paddle to game.
     *
     * @param g the game
     */
    public void addToGame(GameLevel g) {
        g.addSprite(this);
        g.addCollidable(this);
    }
}