
package com.jake.mall.common;

public class MyException extends RuntimeException {

    public MyException() {
    }

    public MyException(String message) {
        super(message);
    }

    /**
     * THROW AN EXCEPTION
     *
     * @param message
     */
    public static void fail(String message) {
        throw new MyException(message);
    }

}
