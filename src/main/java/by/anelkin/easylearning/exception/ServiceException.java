package by.anelkin.easylearning.exception;

/**
 * Serves exception throwing from bottom levels of logic
 *
 *
 * @author Artsiom Anelkin on 2019-08-12.
 * @version 0.1
 */
public class ServiceException extends Exception {
    public ServiceException() {
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }
}
