package com.merle.exception;

import com.merle.scheme.ExpScheme;

public class MyException extends RuntimeException {

    public MyException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public MyException(int result, String msg) {
        super(msg);
        this.result = result;
        this.msg = msg;
    }

    private static final long serialVersionUID = 1L;

    private int result = ExpScheme.UNKNOWN;

    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getResult() {
        return result == 0 ? ExpScheme.UNKNOWN : result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}