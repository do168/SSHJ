package sshj.sshj.common.exception;

public class BusinessException extends RuntimeException {

    private Codes errorCode;

    public BusinessException(String message, Codes errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessException(Codes errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public Codes getErrorCode() {
        return errorCode;
    }

}