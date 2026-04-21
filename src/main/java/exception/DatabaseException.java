package exception;

/**
 * データベース操作に関する例外を実行時例外（RuntimeException）としてラップするクラスです。
 */
public class DatabaseException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
