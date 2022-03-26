package com.jake.mall.util;

import org.springframework.util.StringUtils;


public class AppRes {
    private static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";
    private static final String DEFAULT_FAIL_MESSAGE = "FAIL";
    private static final int RESULT_CODE_SUCCESS = 200;
    private static final int RESULT_CODE_SERVER_ERROR = 500;

    //public static Result<Object> genSuccessResult() {
    public static Result<Object> ok() {
        Result<Object> result = new Result<>();
        result.setResultCode(RESULT_CODE_SUCCESS);
        result.setMessage(DEFAULT_SUCCESS_MESSAGE);
        return result;
    }

    public static Result<String> ok(String message) {
        Result<String> result = new Result<>();
        result.setResultCode(RESULT_CODE_SUCCESS);
        result.setMessage(message);
        return result;
    }

    //public static Result<Object> genSuccessResult(Object data) {
    public static Result<Object> ok(Object data) {
        Result<Object> result = new Result<>();
        result.setResultCode(RESULT_CODE_SUCCESS);
        result.setMessage(DEFAULT_SUCCESS_MESSAGE);
        result.setData(data);
        return result;
    }

    //public static Result<Object> genFailResult(String message) {
    public static Result<Object> error(String message) {
        Result<Object> result = new Result<>();
        result.setResultCode(RESULT_CODE_SERVER_ERROR);
        if (StringUtils.isEmpty(message)) {
            result.setMessage(DEFAULT_FAIL_MESSAGE);
        } else {
            result.setMessage(message);
        }
        return result;
    }

    public static Result<Object> error(int code, String message) {
        Result<Object> result = new Result<>();
        result.setResultCode(code);
        result.setMessage(message);
        return result;
    }
}
