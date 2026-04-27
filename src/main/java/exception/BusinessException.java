package exception;

/**
 * 業務ルール違反やバリデーションエラーなど、
 * ユーザーに通知すべき問題が発生した際にスローされる例外クラスです。
 */
public class BusinessException extends BaseException {
    public BusinessException(String message) {
        super(message);
    }
}
