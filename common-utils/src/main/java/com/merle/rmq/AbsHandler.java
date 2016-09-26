package com.merle.rmq;

public abstract class AbsHandler {
    public abstract boolean process(String message);

}
