package exception;

/**
 * アプリケーションのすべての独自例外の基底クラスです。
 */
public class BaseException extends RuntimeException {
    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
