package menu;

/**
 * Selections of the menu.
 *
 * @param <T> the type parameter
 * @author Daniel Brodsky
 */
public class Selection<T> {
    private String key;
    private String message;
    private T returnVal;

    /**
     * Instantiates a new Selection.
     *
     * @param key       the key
     * @param message   the message
     * @param returnVal the return val
     */
    public Selection(String key, String message, T returnVal) {
        this.key = key;
        this.message = message;
        this.returnVal = returnVal;
    }

    /**
     * Gets key.
     *
     * @return the key
     */
    public String getKey() {
        return this.key;
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public T getValue() {
        return this.returnVal;
    }
}
