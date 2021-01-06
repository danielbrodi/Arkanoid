package game;

import biuoop.DrawSurface;

/**
 * The interface Sprite objects.
 *
 * @author Daniel Brodsky
 */
public interface Sprite {
    /**
     * draws the sprite to the screen.
     *
     * @param d the drawing surface
     */
    void drawOn(DrawSurface d);

    /**
     * notify the sprite that time has passed.
     */
    void timePassed();
}