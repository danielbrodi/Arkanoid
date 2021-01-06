package animation;

import biuoop.DrawSurface;

/**
 * The basic interface of an animation loop.
 * /**
 * * @author Daniel Brodsky
 */

public interface Animation {
    /**
     * Do one frame.
     *
     * @param d the chosen Draw surface.
     */
    void doOneFrame(DrawSurface d);

    /**
     * Should stop the animation depends on a given stat.
     *
     * @return true of false.
     */
    boolean shouldStop();
}
