package sshj.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

// 변경예정인 API 리턴형식
public class ApiResult<T> {

    private final T data;

    private final String error;

    private final Integer code;

    ApiResult(T data, String error, Integer code) {
        this.data = data;
        this.error = error;
        this.code = code;
    }

    public static <T> ApiResult<T> succeed(T data) {
        return new ApiResult<>(data, null, 200);
    }

    public static <T> ApiResult<T> creationSucceed(T data) {
        return new ApiResult<>(data, null, 201);
    }


    public static ApiResult<?> failed(Throwable throwable, Integer code) {
        return failed(throwable.getMessage(), code);
    }

    public static ApiResult<?> failed(String message, Integer code) {
        return new ApiResult<>(null, message, code);
    }

    public T getData() {
        return data;
    }

    public String getError() {
        return error;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("data", data)
                .append("error", error)
                .toString();
    }

}