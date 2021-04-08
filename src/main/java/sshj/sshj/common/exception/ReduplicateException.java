package sshj.sshj.common.exception;

public class ReduplicateException extends BusinessException{
    public ReduplicateException(String message, Codes errorCode) {
        super(message, errorCode);
    }

    public ReduplicateException(Codes errorCode) {
        super(errorCode);
    }
}
