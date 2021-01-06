package collidable;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.awt.Image;
import java.awt.Color;

import ball.Ball;
import listeners.HitListener;
import ball.Velocity;
import biuoop.DrawSurface;
import geometry.Point;
import geometry.Rectangle;
import game.GameLevel;
import listeners.HitNotifier;
import game.Sprite;

import javax.imageio.ImageIO;

/**
 * This class defines what creates a block and how other things in the game act when they reach a block.
 *
 * @author Daniel Brodsky
 */
public class Block implements Collidable, Sprite, HitNotifier {
    private List<HitListener> hitListeners;
    private Color color;
    private Rectangle rectangle;
    private Color colorStroke;
    private String[] images;
    private Image[] loadedImages;
    private Color[] colors;

    /**
     * Instantiates a new collidable Block.
     *
     * @param rectangle a given rectangle
     * @param color     the color of the block
     */
    public Block(Rectangle rectangle, Color color) {
        this.color = color;
        this.rectangle = rectangle;
        hitListeners = new ArrayList<>();
    }

    /**
     * Instantiates a new Block.
     *
     * @param leftUpperCorner the left upper corner
     * @param width           the width
     * @param height          the height
     */
    public Block(Point leftUpperCorner, int width, int height) {
        this.rectangle = new Rectangle(leftUpperCorner, width, height);
        hitListeners = new ArrayList<>();
    }

    /**
     * Adds the block to the game.
     *
     * @param g the given level and game.
     */
    public void addToGame(GameLevel g) {
        g.addCollidable(this);
        g.addSprite(this);
    }

    /**
     * Remove from game.
     *
     * @param game the game
     */
    public void removeFromGame(GameLevel game) {
        game.removeCollidable(this);
        game.removeSprite(this);
    }

    /**
     * Remove from sprites.
     *
     * @param game the game
     */
    public void removeFromSprites(GameLevel game) {
        game.removeSprite(this);
    }

    /**
     * gets the selected block info.
     *
     * @return the rectangle
     */
    public Rectangle getCollisionRectangle() {

        return this.rectangle;
    }

    /**
     * Color for stroke.
     *
     * @param c is the color to set.
     */
    public void colorForStroke(Color c) {
        this.colorStroke = c;
    }


    /**
     * Sets colors.
     *
     * @param changingBlockColorFill the changing block color fill
     */
    public void setColors(Color[] changingBlockColorFill) {
        this.colors[0] = changingBlockColorFill[0];
    }

    /**
     * Sets color.
     *
     * @param c the c
     */
    public void setColor(Color c) {
        this.color = c;
    }

    /**
     * Sets images paths.
     *
     * @param s is an array of images paths to set.
     */
    public void setImages(String[] s) {
        this.images = s;
    }

    /**
     * Load images to block.
     *
     * @param paths the paths to an images
     */
    public void loadImagesToBlock(String[] paths) {
        try {
            List<Image> imageList = new ArrayList<>();
            for (int i = 0; i < paths.length; i++) {
                Image image;
                trimStrings(paths);
                InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(paths[i]);
                image = ImageIO.read(is);
//                image = ImageIO.read(new File("resources/" + paths[i]));
                imageList.add(image);
            }
            this.loadedImages = new Image[imageList.size()];
            this.loadedImages = imageList.toArray(this.loadedImages);

        } catch (IOException e) {
            System.out.println("failed loading image while drawing blocks");
        }
    }

    /**
     * A function which will be called whenever a hit() occurs,
     * and notifiers all of the registered listeners.HitListener objects by calling their hitEvent method.
     *
     * @param hitter the ball.Ball that hits the block
     */
    private void notifyHit(Ball hitter) {
        // Make a copy of the hitListeners before iterating over them.
        List<HitListener> listeners = new ArrayList<>(this.hitListeners);
        // Notify all listeners about a hit event:
        for (HitListener hl : listeners) {
            hl.hitEvent(this, hitter);
        }
    }

    /**
     * trims a given string and removes any special symbols.
     *
     * @param s the trimmed string
     */
    private void trimStrings(String[] s) {
        for (int i = 0; i < s.length; i++) {
            s[i] = s[i].replaceAll("\\(", "");
            s[i] = s[i].replaceAll("\\)", "");
        }
    }


    /**
     * this function changes the velocity of a given object that reaches a block, based on from which side
     * it  hits the block.
     *
     * @param collisionPoint  the collision point between an object and the block
     * @param currentVelocity the velocity of the object that hits the block
     * @param hitter          the ball that hits the block.
     * @return new velocity of the object that hits the block
     */


    public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity) {
        Velocity newVelocity = currentVelocity;

        // starting point - according to the upper left point of the rectangle
        double startX = rectangle.getUpperLeft().getX();
        double startY = rectangle.getUpperLeft().getY();

        double x = collisionPoint.getX();
        double y = collisionPoint.getY();

        // Y direction
        if ((x > startX) && (x < startX + rectangle.getWidth())) {
            newVelocity.setDy((currentVelocity.getDy()) * (-1));
        }
        // X direction
        if ((y > startY) && (y < startY + rectangle.getHeight())) {
            newVelocity.setDx(currentVelocity.getDx() * (-1));
        }
        // hits the corners
//        else {
//            newVelocity.setDx(currentVelocity.getDx() * (-1));
//            newVelocity.setDy(currentVelocity.getDy() * (-1));
//        }

        this.notifyHit(hitter);
        return newVelocity;

    }

    /**
     * draws the block on the given surface.
     *
     * @param d the drawinng surface
     */

    public void drawOn(DrawSurface d) {
        if (this.images != null && this.images.length > 0) {
            drawOnImage(d); // one image
        } else {
            d.setColor(color);
            d.drawRectangle((int) rectangle.getUpperLeft().getX(),
                    (int) rectangle.getUpperLeft().getY(),
                    (int) rectangle.getWidth(), (int) rectangle.getHeight());
            d.fillRectangle((int) rectangle.getUpperLeft().getX(),
                    (int) rectangle.getUpperLeft().getY(),
                    (int) rectangle.getWidth(), (int) rectangle.getHeight());
            if (this.colorStroke != null) {
                d.setColor(this.colorStroke);
            } else {
                d.setColor((color));
            }
            d.drawRectangle((int) rectangle.getUpperLeft().getX(),
                    (int) rectangle.getUpperLeft().getY(),
                    (int) rectangle.getWidth(), (int) rectangle.getHeight());
        }
    }

    /**
     * nothing meanwhile.
     */
    public void timePassed() {
//nothing (as you asked).
    }

    /**
     * This method block's image.
     *
     * @param d is the surface to draw the image to.
     */
    private void drawOnImage(DrawSurface d) {
        int index = 0;
        Point p = rectangle.getUpperLeft();
        d.drawImage((int) p.getX(), (int) p.getY(), this.loadedImages[index]);
        if (this.colorStroke != null) {
            int width = (int) this.rectangle.getWidth();
            int height = (int) this.rectangle.getHeight();
            d.drawRectangle((int) p.getX(), (int) p.getY(), width, height);
        }

    }

    /**
     * adds the block as a hit listener.
     *
     * @param hl the listener
     */
    public void addHitListener(HitListener hl) {
        hitListeners.add(hl);
    }

    /**
     * removes the block from being a hit listener.
     *
     * @param hl the listener
     */
    public void removeHitListener(HitListener hl) {
        hitListeners.remove(hl);
    }
}
