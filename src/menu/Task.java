package menu;

/**
 * The interface Task.
 *
 * @param <T> the type parameter
 * @author Daniel Brodsky
 */
public interface Task<T> {
    /**
     * Run t.
     *
     * @return the generic.
     * @throws Exception the exception
     */
    T run() throws Exception;
}
