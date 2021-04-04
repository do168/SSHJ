package sshj.sshj.common.exception;

public enum Codes {
    //TODO: 에러 CODE 정의 필요
    INTERNAL_SERVER_ERROR(500, "I001", " INTERNAL SERVER ERROR"),
    CLUB_NOT_FOUND(400,"CLUB01", " CLUB NOT FOUND"),
    INVALID_INPUT_VALUE(400, "C001", " Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "C002", " Invalid Input Value"),
    HANDLE_ACCESS_DENIED(403, "C006", "Access is Denied"),
    INVALID_ENUM_TYPE(400, "C003", " INVALID ENUM TYPE"),
    ;
    private final String code;
    private final String message;
    private int status;

    Codes(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getCode() {
        return code;
    }

    public int getStatus() {
        return status;
    }

}
