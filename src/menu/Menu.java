package menu;

import animation.Animation;

/**
 * The interface Menu.
 *
 * @param <T> the type parameter
 * @author Daniel Brodsky
 */
public interface Menu<T> extends Animation {
    /**
     * Add selection.
     *
     * @param key       the key
     * @param message   the shown text
     * @param returnVal the return value
     */
    void addSelection(String key, String message, T returnVal);

    /**
     * Gets status.
     *
     * @return the status
     */
    T getStatus();
}
