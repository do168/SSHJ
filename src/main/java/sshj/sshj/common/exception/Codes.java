package sshj.sshj.common.exception;

public enum Codes {
    //TODO: 에러 CODE 정의 필요
    // internal
    INTERNAL_SERVER_ERROR(500, "I001", " INTERNAL SERVER ERROR"),
    // global
    NOT_FOUND_ERROR(400,"G01", " CLUB NOT FOUND"),
    /*
            409 Conflict 코드는 리소스가 충돌이 발생하였고 사용자가 이를 반영할 수 있을 때 발생하는 상태 코드입니다.
            아이디 중복 체크에서는 사용자가 충돌되지 않는 다른 아이디를 사용하여 충돌을 해결해야 하기 때문에 이 상태 코드도 적합하다고 생각하였습니다.
            409 코드에 대하여 조사해보니 다른 웹 사이트에서도 ID 중복 발생 시 409 코드를 많이 쓴다고 현업자 분들의 의견이 달려있는 것을 확인하였습니다.
    */
    REDUPLICATE_ERROR(409, "G02", "REDUPLICATED"),
    // common
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
