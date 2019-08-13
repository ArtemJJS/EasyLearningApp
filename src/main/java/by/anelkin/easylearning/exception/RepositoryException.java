package by.anelkin.easylearning.exception;

/**
 * Serves exception throwing from bottom levels of logic
 *
 *
 * @author Artsiom Anelkin on 2019-08-12.
 * @version 0.1
 */
public class RepositoryException extends Exception{
    public RepositoryException() {
    }

    public RepositoryException(String message) {
        super(message);
    }

    public RepositoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public RepositoryException(Throwable cause) {
        super(cause);
    }
}
