package ball;

import biuoop.DrawSurface;
import collidable.CollisionInfo;
import geometry.Line;
import geometry.Point;
import game.GameLevel;
import game.GameEnvironment;
import game.Sprite;

import java.awt.Color;

/**
 * This class defines what creates a ball and how to create, move and draw it.
 *
 * @author Daniel Brodsky
 */
public class Ball implements Sprite {
    private final int r;
    /**
     * The center point of the ball.
     */
    private Point center;
    /**
     * The Color of the ball.
     */
    private java.awt.Color color;
    private Velocity velocity;
    private GameEnvironment gameEnvironment;

    /**
     * Instantiates a new ball.Ball.
     * A constructor
     *
     * @param center          the center point of the ball
     * @param r               the radius
     * @param color           the color
     * @param gameEnvironment the game environment that the ball joins to
     * @param v               the velocity of the ball
     */
    public Ball(Point center, int r, java.awt.Color color, GameEnvironment gameEnvironment, Velocity v) {
        this.center = center;
        this.color = color;
        this.r = r;
        this.setVelocity(v.getDx(), v.getDy());
        this.gameEnvironment = gameEnvironment;
    }

    /**
     * Adds the ball to game.
     *
     * @param g the game
     */
    public void addToGame(GameLevel g) {
        g.addSprite(this);
    }

    /**
     * Gets the X coordinate of the center point of the ball.
     *
     * @return the x
     */
    public int getX() {
        return (int) this.center.getX();
    }


    /**
     * Removes the ball from the game..
     *
     * @param game the game
     */
    public void removeFromGame(GameLevel game) {
        game.removeSprite(this);
    }

    /**
     * Gets y coordinate of the center point of the ball.
     *
     * @return the y
     */
    public int getY() {
        return (int) this.center.getY();
    }

    /**
     * Gets size of the ball, which means the radius of it.
     *
     * @return the size
     */
    public int getSize() {
        return this.r; //gets the size of the radius
    }

    /**
     * Gets the color of the ball.
     *
     * @return the color
     */
    public java.awt.Color getColor() {
        return this.color;
    }

    /**
     * draws the ball on the given surface.
     *
     * @param surface the drawing surface
     */
    public void drawOn(DrawSurface surface) {
        surface.setColor(getColor());
        surface.fillCircle(getX(), getY(), getSize());
        surface.setColor(Color.black);
        surface.drawCircle(getX(), getY(), getSize());
    }

    /**
     * moves the ball.
     */
    public void timePassed() {
        moveOneStep();
    }

    /**
     * Sets velocity of the ball by a given velocity.
     *
     * @param v the velocity
     */
    public void setVelocity(Velocity v) {
        this.velocity = v;
    }

    /**
     * Sets velocity of the ball by a given Dx and Dy values.
     *
     * @param dx the dx
     * @param dy the dy
     */
    public void setVelocity(double dx, double dy) {
        this.velocity = new Velocity(dx, dy);
    }

    /**
     * Gets velocity of the ball.
     *
     * @return the velocity
     */
    public Velocity getVelocity() {
        return this.velocity;
    }

    /**
     * moves the ball to "almost" the given hit point, but just slightly before it.
     *
     * @param centerPoint the center point of the ball
     * @return the new point which is slightly before the given hit point.
     */
    public Point moveNear(Point centerPoint) {
        double xCenter = center.getY();
        double yCenter = center.getY();
        if (this.getVelocity().getDx() > 0) {
            xCenter = center.getX() - 1;
        }
        if (this.getVelocity().getDx() < 0) {
            xCenter = center.getX() + 1;
        }
        if (this.getVelocity().getDy() > 0) {
            yCenter = center.getY() - 1;
        }
        if (this.getVelocity().getDy() < 0) {
            yCenter = center.getY() + 1;
        }
        return new Point(xCenter, yCenter);
    }

    /**
     * computes the ball trajectory (the trajectory is "how the ball will move
     * without any obstacles" -- its a line starting at current location, and
     * ending where the velocity will take the ball if no collisions will occur).
     * Checks (using the game environment) if moving on this trajectory the ball will hit anything.
     * If no, the ball will move to the end of the trajectory.
     * Otherwise(there is a hit) , the ball will move slightly before the hit point, and change
     * it's velocity to the opposite direction to avoid getting out of bounds or into the obstacle.
     */
    public void moveOneStep() {
        Velocity currentVelocity = this.getVelocity();
//        System.out.println("(" + this.getX() + "," + this.getY() + ") dx: " + this.velocity.getDx()
//                + " dy: " + this.velocity.getDy());
        double x = this.center.getX();
        double y = this.center.getY();
        Line trajectory = new Line(new Point(x, y),
                new Point(x + currentVelocity.getDx(),
                        y + currentVelocity.getDy()));
        // get the closest collision point between the line and the rectangle
        CollisionInfo collisionInfo = this.gameEnvironment.getClosestCollision(trajectory);

        if (collisionInfo != null) {
            // there's a collision
            this.center = moveNear(this.center);
            currentVelocity = collisionInfo.collisionObject().
                    hit(this, collisionInfo.collisionPoint(), currentVelocity);
            this.velocity = currentVelocity;
        }
        this.center = this.velocity.applyToPoint(this.center);
    }
}
