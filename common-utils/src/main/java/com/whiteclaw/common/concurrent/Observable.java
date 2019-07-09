package com.whiteclaw.common.concurrent;

/**
 * @author whiteclaw
 * created on 2019-01-04
 */
public interface Observable {
    enum Cycle {
        STARTED, RUNNING, DONE, ERROR
    }

    /**
     * 获取当前的线程生命周期
     */
    Cycle getCycle();

    /**
     * 开始执行线程
     */
    void start();

    /**
     * 打断线程
     */
    void interrupt();
}
