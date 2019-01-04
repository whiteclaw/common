package com.whiteclaw.concurrent;

/**
 * TaskLifeCycle的空实现
 *
 * @author whiteclaw
 * created on 2019-01-04
 */
public class EmptyTaskLifeCycle<T> implements TaskLifeCycle<T> {

    @Override
    public void onStart(Thread thread) {
        // do something...
    }

    @Override
    public void onRunning(Thread thread) {
        // do something...
    }

    @Override
    public void onFinished(Thread thread, T result) {
        // do something...
    }

    @Override
    public void onError(Thread thread, Exception e) {
        // do something...
    }
}
